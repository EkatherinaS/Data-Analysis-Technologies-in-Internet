namespace Lab2
{
    internal interface IXPathQuery
    {
        public string getXPathResult(string xpath);

        public string getAllLessons();

        public string getAllRooms();

        public string getAllPractice();

        public string getAllLessonsInRoom(string room);

        public string getAllProfessorsInRoom(string room);

        public string getLastSubjects();

        public string getLessonAmount();
    }
}
