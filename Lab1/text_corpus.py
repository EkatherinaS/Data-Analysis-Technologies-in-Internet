import os

import text_file as tf
import text_dictionary as td


def get_corpus_text(directory):
    text = ""
    for filename in os.listdir(directory):
        text += tf.read_file(directory + "/" + filename) + "\n"
    return text


def get_key_words(directory, norm_option="normalized"):
    td_dict = {}
    idf_dict = {}

    for filename in os.listdir(directory):
        text = tf.read_file(tf.get_path(directory, filename))
        if norm_option == "stemmed":
            norm_dictionary = td.create_stemmed_dictionary(text)
        else:
            norm_dictionary = td.create_normalized_dictionary(text)

        td_dict = {k: norm_dictionary.get(k, 0) + td_dict.get(k, 0)
                   for k in set(norm_dictionary) | set(td_dict)}
        idf_dict = {k: idf_dict.get(k, 0) + (1 if k in norm_dictionary else 0)
                    for k in set(norm_dictionary) | set(idf_dict)}

    n = len(os.listdir(directory))
    td_idf_dict = {k: td_dict.get(k, 0) * n / idf_dict.get(k, 0) for k in set(idf_dict)}
    return td_idf_dict
