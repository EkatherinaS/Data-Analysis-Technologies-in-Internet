import re
import text_corpus as tc
import text_dictionary as td


def sentence_extraction(texts_dir, compression):
    """
    Входные данные: исходный текст, коэффициент сжатия
    Выходные данные: список ключевых слов с весами, список предложений с весами, текст реферата.
    """

    text = tc.get_corpus_text(texts_dir)

    # 1.      Разбить текст на предложения.
    sentences = re.findall('[А-Я].*?[?!.…]', text)

    # 2.      Разбить текст на слова произвести их нормализацию.
    # 3.      Удалить стоп-слова.
    # 4.      Подсчитать веса слов (tf или tf-idf).
    tf_idf_result = tc.get_key_words(texts_dir, "normalized")

    # 5.      Определить веса предложений, рассчитанный как сумма весов, входящих в предложение слов.
    sentence_dict = {}
    for sentence in sentences:
        norm_words = td.create_normalized_dictionary(sentence)
        sentence_dict[sentence] = sum(tf_idf_result.get(key, 0) * norm_words.get(key, 0)
                                      for key in set(norm_words.keys()))

    # 6.      Отсортировать предложения по убыванию веса.
    result_n = len(sentences) * compression
    sorted_sentences = sorted(sentence_dict.items(), key=lambda x: x[1], reverse=True)
    text_result = ""
    for (key, value) in sorted_sentences:
        if result_n > 0:
            text_result += key + "\n"
        else:
            break
        result_n -= 1
    sentence_result = td.dictionary_to_string(sentence_dict)
    return td.dictionary_to_string(tf_idf_result), sentence_result, text_result

