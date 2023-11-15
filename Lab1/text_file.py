import re


def create_file(path):
    text_file = open(path, "w+")
    text_file.close()


def read_file(path):
    text_file = open(path, "r")
    text = text_file.read()
    text_file.close()
    return text


def write_file(text, path, filename):
    text_file = open(get_path(path, filename), "w+")
    text_file.write(text)
    text_file.close()


def get_path(path, filename):
    filename = re.match(r'\b[^.]+', filename)[0]
    return "{0}/{1}.txt".format(path, filename)
