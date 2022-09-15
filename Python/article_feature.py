import pymysql
import pickle
import sys

from sklearn.decomposition import PCA
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
 

def train(samples):

	vocab = {
		'游戏':0,'时尚':1,'穿搭':2,'音乐':3,'男性':4,'发型':5,'动漫':6,'食物':7,'情感':8,'体育':9,
        '科技':10,'数码':11,'摄影':12,'旅行':13,'搞笑':14,'电影':15,'健身':16,'明星':17,'手工':18,'科普':19,
        '绘画':20,'教育':21,'汽车':22,'宠物':23,'壁纸':24,'头像':25,'舞蹈':26,'艺术':27,'校园':28,'心理':29,
        '女性':30,'美术':31,'生活':32,'潮玩':33,'文具':34,'户外':35,'综艺':36,'极限':37,'商业':38,'职场':39,
        '原神':40,'美食':41,'学习':42,'大学':43,'高中':44,'初中':45,'恋爱':46,'手办':47,'兴趣':48,'礼物':49,
        '世界':50,'中国':51,'主人':52,'主角':53,'产品':54,'产生':55,'人员':56,'人生':57,'人类':58,'人物':59,
        '代表':60,'任务':61,'休息':62,'伤害':63,'位置':64,'体验':65,'作品':66,'作用':67,'作者':68,'信息':69,
        '公主':70,'公司':71,'分享':72,'剧情':73,'力量':74,'功能':75,'动作':76,'动画':77,'历史':78,'发布':79,
        '吃饭':80,'同学':81,'哥哥':82,'回家':83,'国家':84,'图片':85,'城市':86,'大人':87,'黑色':88,'头发':89,
        '女人':90,'女孩':91,'女生':92,'妈妈':93,'姐姐':94,'安全':95,'官方':96,'对手':97,'小伙伴':98,'小米':99,
        '少女':100,'少年':101,'工作':102,'平台':103,'微信':104,'心情':105,'怪物':106,'感情':107,'战争':108,'战士':109,
        '战斗':110,'手机':111,'技术':112,'技能':113,'推荐':114,'故事':115,'敌人':116,'文化':117,'文章':118,'日本':119,
        '时代':120,'晚上':121,'朋友':122,'未来':123,'模式':124,'欢迎':125,'武器':126,'死亡':127,'比赛':128,'活动':129,
        '测试':130,'灵魂':131,'照片':132,'爱情':133,'父亲':134,'父母':135,'环境':136,'生命':137,'生气':138,'黑暗':139,
        '电话':140,'男人':141,'皇帝':142,'皮肤':143,'眼睛':144,'睡觉':145,'社会':146,'空间':147,'笑容':148,'粉丝':149,
        '精神':150,'网络':151,'美国':152,'老师':153,'职业':154,'英雄':155,'衣服':156,'装备':157,'观众':158,'视频':159,
        '角色':160,'计划':161,'训练':162,'记忆':163,'调整':164,'距离':165,'输出':166,'速度':167,'队友':168,'阶段':169,
        '项目':170,'风格':171,'骑士':172,'魔法':173
	}

	vectorizer = CountVectorizer(max_features=174, vocabulary=vocab)
	vectorizer.fit(samples)

	with open('./Python/vectorizer.pickle', 'wb') as f:
		pickle.dump(vectorizer, f)

	count_mat = vectorizer.transform(samples)

	tf_idf_transformer = TfidfTransformer()
	tf_idf = tf_idf_transformer.fit(count_mat)

	with open('./Python/tf_idf.pickle', 'wb') as f:
		pickle.dump(tf_idf, f)

	samples = tf_idf.transform(count_mat).toarray()

	pca = PCA(n_components=50)
	pca.fit(samples)

	with open('./Python/pca.pickle', 'wb') as f:
		pickle.dump(pca, f)


def transform(samples):

	with open('./Python/vectorizer.pickle', 'rb') as f:
		vectorizer = pickle.load(f)

	with open('./Python/tf_idf.pickle', 'rb') as f:
		tf_idf = pickle.load(f)

	with open('./Python/pca.pickle', 'rb') as f:
		pca = pickle.load(f)

	count_mat = vectorizer.transform(samples)

	samples = tf_idf.transform(count_mat).toarray()

	return [list(i) for i in list(pca.transform(samples))]



args = sys.argv
fun_name = args[1]
article_id = args[2]


db = pymysql.connect(host='localhost', user='root',
                     password='121522734a', port=3306, db='Enclusiv')
cursor = db.cursor()


if fun_name == "train":
	cursor.execute("select * from article_words")
	samples = [i[2] for i in cursor.fetchall()]
	train(samples)

if fun_name == "transform":
	cursor.execute("select words from article_words where article_id=%s", article_id)

	article_words = cursor.fetchone()[0]

	features = list(transform([article_words])[0])

	features_str = "["
	for num in features:
		features_str += str(num) + ','
	features_str = features_str[:-1] + ']'

	try:
		cursor.execute("insert into article_features (article_id, features) values (%s, %s) ", (article_id, features_str))
		db.commit()
	except pymysql.err.IntegrityError:
		print("IntegrityError")
		pass


print("finish")





