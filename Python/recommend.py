import numpy as np
from numpy.linalg import norm
import pymysql
import sys


def cos_sim(v1, v2):
    n1, n2 = norm(v1), norm(v2)
    if (n1 == 0 or n2 == 0):
        return 0
    return (v1 @ v2.T).item() / (norm(v1) * norm(v2))


def algo(v1, v2, v3, v4):
	num = len(v1) + len(v2) * 3 + len(v3) * 5
	v1_final = 0
	v2_final = 0
	v3_final = 0

	if len(v1) > 0:
		v1 = np.array(v1)
		v1_final = v1[0]
		for i in v1[1:]:
			v1_final = v1_final + i

	if len(v2) > 0:
		v2 = np.array(v2)
		v2_final = v2[0]*3
		for i in v2[1:]:
			v2_final = v2_final + i * 3

	if len(v3) > 0:
		v3 = np.array(v3)
		v3_final = v3[0]*5
		for i in v3[1:]:
			v3_final = v3_final + i * 5

	v_final = v1_final + v2_final + v3_final

	v_final = v_final / num

	v4 = np.array(v4)
	all_sim_1 = [cos_sim(v, v_final) for v in v4]

	return all_sim_1



def get_article_id(cursor, user_id, author_id, type, collection_ids, like_ids):
	data = {}
	if user_id == 0:
		cursor.execute("select user_id, article_id from user, user_" + type + " where user.id != %s and user_" + type + ".user_id=user.id", author_id)
	else:
		cursor.execute("select user_id, article_id from user, user_" + type + " where user.id != %s and user.id=%s and user_" + type + ".user_id=user.id", (author_id, user_id))

	for i in cursor.fetchall():
		ids = data.get(i[0], [])
		c_ids = collection_ids.get(i[0], [])
		l_ids = like_ids.get(i[0], [])
		if i[1] not in c_ids and i[1] not in l_ids:
			ids.append(i[1])
			data[i[0]] = ids
	return data



def get_article_features(cursor, article_ids):
	data = {}
	for user_id, ids in article_ids.items():
		features = []
		for id in ids:
			cursor.execute("select features from article_features where article_id=%s", id)
			features.append(eval(cursor.fetchone()[0]))
		data[user_id] = features
	return data



def all_user_one_article(cursor, author_id, article_id):
	collection_ids = get_article_id(cursor, 0, author_id, "collect", {}, {})

	like_ids = get_article_id(cursor, 0, author_id, "like", collection_ids, {})

	view_ids = get_article_id(cursor, 0, author_id, "view", collection_ids, like_ids)

	collection_features = get_article_features(cursor, collection_ids)

	like_features = get_article_features(cursor, like_ids)

	view_features = get_article_features(cursor, view_ids)

	for user_id in view_features.keys():

		cursor.execute("select id from article where author_id=%s", user_id)
		user_article_ids = [i[0] for i in cursor.fetchall()]

		ids = []
		ids.extend(view_ids.get(user_id, []))
		ids.extend(like_ids.get(user_id, []))
		ids.extend(collection_ids.get(user_id, []))
		ids.extend(user_article_ids)
		ids = set(ids)
		cursor.execute("select features from article_features where article_id=%s", article_id)
		article_features = [eval(cursor.fetchone()[0])]

		if int(article_id) not in ids:
			score = algo(view_features.get(user_id, []), like_features.get(user_id, []), collection_features.get(user_id, []), article_features)[0]
			print(user_id)
			print(article_id, score)
			print("---------------------------------")


def all_user_all_article(cursor):

	collection_ids = get_article_id(cursor, 0, 0, "collect", {}, {})

	like_ids = get_article_id(cursor, 0, 0, "like", collection_ids, {})

	view_ids = get_article_id(cursor, 0, 0, "view", collection_ids, like_ids)

	collection_features = get_article_features(cursor, collection_ids)

	like_features = get_article_features(cursor, like_ids)

	view_features = get_article_features(cursor, view_ids)

	for user_id in view_features.keys():
		cursor.execute("select id from article where author_id=%s", user_id)
		user_article_ids = [i[0] for i in cursor.fetchall()]

		ids = []
		ids.extend(view_ids.get(user_id, []))
		ids.extend(like_ids.get(user_id, []))
		ids.extend(collection_ids.get(user_id, []))
		ids.extend(user_article_ids)
		ids = set(ids)

		cursor.execute("select article_id, features from article_features")
		features_data = cursor.fetchall()
		article_features = []
		article_ids = []
		for i in range(len(features_data)):
			if int(features_data[i][0]) not in ids:
				article_features.append(eval(features_data[i][1]))
				article_ids.append(features_data[i][0])
		scores = algo(view_features.get(user_id, []), like_features.get(user_id, []), collection_features.get(user_id, []), article_features)

		print(user_id)
		for i in range(len(scores)):
			print(article_ids[i], scores[i])
		print("---------------------------------")




def one_user_all_article(cursor, user_id):
	collection_ids = get_article_id(cursor, user_id, 0, "collect", {}, {})

	like_ids = get_article_id(cursor, user_id, 0, "like", collection_ids, {})

	view_ids = get_article_id(cursor, user_id, 0, "view", collection_ids, like_ids)

	collection_features = get_article_features(cursor, collection_ids)

	like_features = get_article_features(cursor, like_ids)

	view_features = get_article_features(cursor, view_ids)

	cursor.execute("select id from article where author_id=%s", user_id)
	user_article_ids = [i[0] for i in cursor.fetchall()]
	ids = []
	ids.extend(view_ids.get(user_id, []))
	ids.extend(like_ids.get(user_id, []))
	ids.extend(collection_ids.get(user_id, []))
	ids.extend(user_article_ids)
	ids = set(ids)

	cursor.execute("select article_id, features from article_features")
	features_data = cursor.fetchall()
	article_features = []
	article_ids = []
	for i in range(len(features_data)):
		if int(features_data[i][0]) not in ids:
			article_features.append(eval(features_data[i][1]))
			article_ids.append(features_data[i][0])
	scores = algo(view_features.get(user_id, []), like_features.get(user_id, []), collection_features.get(user_id, []), article_features)

	print(user_id)
	for i in range(len(scores)):
		print(article_ids[i], scores[i])
	print("---------------------------------")


args = sys.argv
type = args[1]

db = pymysql.connect(host='localhost', user='root',
                     password='121522734a', port=3306, db='Enclusiv')
cursor = db.cursor()

if type == "one_all":
	user_id = args[2] # 上传文章的用户的id
	one_user_all_article(cursor, int(user_id))

if type == "all_one":
	author_id = args[2] # 上传文章的用户的id
	article_id = args[3]
	all_user_one_article(cursor, author_id, article_id)

if type == "all_all":
	all_user_all_article(cursor)
