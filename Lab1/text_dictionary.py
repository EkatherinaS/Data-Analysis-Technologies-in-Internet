import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem.porter import PorterStemmer
import pymorphy2
import re


normalizer = pymorphy2.MorphAnalyzer()
stemmer = PorterStemmer()


def download_files():
    nltk.download('stopwords')
    nltk.download('punkt')


def tokenize_text(text):
    pattern = re.compile(r'\b[А-Яа-яA-Za-z]+(-?[А-Яа-яA-Za-z]+)*\b')
    tokenization = [token for token in word_tokenize(text)
                    if token not in stopwords.words('russian')
                    and pattern.match(token)]
    return tokenization


def create_stemmed_dictionary(text):
    dictionary = {}
    for token in tokenize_text(text):
        stemmed = stemmer.stem(token)
        if stemmed not in dictionary.keys():
            dictionary[stemmed] = 1
        else:
            dictionary[stemmed] += 1
    return dictionary


def create_normalized_dictionary(text):
    dictionary = {}
    for token in tokenize_text(text):
        normalized = normalizer.parse(token)[0].normal_form
        if normalized not in dictionary.keys():
            dictionary[normalized] = 1
        else:
            dictionary[normalized] += 1
    return dictionary


def compare_dictionaries(normalized, stemmed):
    normalized_keys = set(normalized.keys())
    stemmed_keys = set(stemmed.keys())

    keys_common = [key + ": normalized - " + str(normalized.get(key)) +
                   " stemmed - " + str(normalized.get(key))
                   for key in set(normalized_keys & stemmed_keys)
                   if normalized[key] != stemmed[key]]
    only_normalized = [key + " - " + str(normalized.get(key)) for key in set(normalized_keys - stemmed_keys)]
    only_stemmed = [key + " - " + str(stemmed.get(key)) for key in set(stemmed_keys - normalized_keys)]

    return {'common': keys_common, 'normalized': only_normalized, 'stemmed': only_stemmed}


def sort_dictionary(dictionary):
    sorted_dict = sorted(dictionary.items(), key=lambda x: x[1], reverse=True)
    return sorted_dict


def dictionary_to_string(dictionary):
    text = ""
    sorted_keys = sort_dictionary(dictionary)
    for (key, value) in sorted_keys:
        text += key + ": " + str(value) + "\n"
    return text
