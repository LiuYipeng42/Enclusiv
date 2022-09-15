import sys
import pymysql
from pyhanlp import JClass, HanLP, HANLP_DATA_PATH, DoubleArrayTrieSegment


def load_from_file(path):
    """
    从词典文件加载DoubleArrayTrie
    :param path: 词典路径
    :return: 双数组trie树
    """
    map = JClass('java.util.TreeMap')()  # 创建TreeMap实例
    with open(path) as src:
        for word in src:
            word = word.strip()  # 去掉Python读入的\n
            map[word] = word
    return JClass('com.hankcs.hanlp.collection.trie.DoubleArrayTrie')(map)


# 去掉停用词
def remove_stopwords_termlist(termlist, trie):

	words = []
	for term in termlist:
		try:
			if not trie.containsKey(term.word):
				words.append(term.word)
		except UnicodeDecodeError:
			pass

	return words


def word_segmentation(article):
	HanLP.Config.ShowTermNature = False
	dict1 = HANLP_DATA_PATH + "/dictionary/CoreNatureDictionary.mini.txt"
	segment = DoubleArrayTrieSegment([dict1])

	trie = load_from_file("./Python/stopwords.txt")

	text = ""
	line = ""
	for i in article:
		if i != '\n':
			line += i
		else:
			if len(line) > 0 and '<img src="' not in line:
				text += line + "\n"
			line = ""
	if len(text) == 0:
		text = line

	termlist = segment.seg(text)

	new_article = ""
	for word in remove_stopwords_termlist(termlist, trie):
		if word != '\n' and word != '\xa0' and word != ' ':
			new_article += word + " "

	return new_article


args = sys.argv
article_id = args[1]


db = pymysql.connect(host='localhost', user='root',
                     password='121522734a', port=3306, db='Enclusiv')
cursor = db.cursor()

cursor.execute("select article from article where id=%s", article_id)

article = cursor.fetchone()[0]

words = word_segmentation(article)

try:
	cursor.execute("insert into article_words (article_id, words) values (%s, %s) ", (article_id, words))
	db.commit()
except pymysql.err.IntegrityError:
	print("IntegrityError")
	pass

print("finish")



