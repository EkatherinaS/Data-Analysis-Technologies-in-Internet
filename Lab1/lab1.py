import text_file as tf
import text_dictionary as td
import text_corpus as tc
import text_sentence_extraction as se


def task_9(texts_dir, output_dir):
    output_filename = "task_9"
    text = tc.get_corpus_text(texts_dir)
    normalized_dict = td.create_normalized_dictionary(text)
    result = td.dictionary_to_string(normalized_dict)
    tf.write_file(result, output_dir, output_filename)


def task_10(texts_dir, output_dir):
    output_filename = "task_10"
    text = tc.get_corpus_text(texts_dir)
    dictionary_normalized = td.create_normalized_dictionary(text)
    dictionary_stemmed = td.create_stemmed_dictionary(text)

    result = "Normalized:\n" + \
                  td.dictionary_to_string(dictionary_normalized) + \
                  "\n\n" + \
                  "Stemmed:\n" + \
                  td.dictionary_to_string(dictionary_stemmed)

    result += "\n\nDIFFERENCE"
    difference = td.compare_dictionaries(dictionary_normalized, dictionary_stemmed)

    result += "\n\nOnly normalized:\n"
    for value in difference['normalized']:
        result += value + "\n"

    result += "\n\nOnly stemmed:\n"
    for value in difference['stemmed']:
        result += value + "\n"

    result += "\n\nCommon:\n"
    for value in difference['common']:
        result += value + "\n"

    tf.write_file(result, output_dir, output_filename)


def task_11(texts_dir, output_dir):
    output_filename = "task_11"
    td_idf_normalized_dict = tc.get_key_words(texts_dir, "normalized")
    td_idf_stemmed_dict = tc.get_key_words(texts_dir, "stemmed")
    result = 'Normalized key words:\n{0}\n\nStemmed key words:\n{1}'\
        .format(td.dictionary_to_string(td_idf_normalized_dict),
                td.dictionary_to_string(td_idf_stemmed_dict))
    tf.write_file(result, output_dir, output_filename)


def task_12(texts_dir, output_dir):
    output_filename = "task_12"
    compression = 0.1
    tf_idf_result, sentence_result, text_result = se.sentence_extraction(texts_dir, compression)
    result = "Normalized words:\n{0}\n\nSentences:\n{1}\n\nFinal text:\n{2}"\
        .format(str(tf_idf_result), sentence_result, text_result)
    tf.write_file(result, output_dir, output_filename)
