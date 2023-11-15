import lab1


output_dir = "results"
texts_dir = "corpus_of_texts"
task_completed = "Задание {0} выполнено, результат расположен в папке results: task_{0}"

lab1.task_9(texts_dir, output_dir)
print(task_completed.format(9))
lab1.task_10(texts_dir, output_dir)
print(task_completed.format(10))
lab1.task_11(texts_dir, output_dir)
print(task_completed.format(11))
lab1.task_12(texts_dir, output_dir)
print(task_completed.format(12))
