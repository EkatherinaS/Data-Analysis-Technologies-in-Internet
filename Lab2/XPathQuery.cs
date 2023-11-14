using System.Xml.XPath;

namespace Lab2
{
    public class XPathQuery
    {
        XPathNavigator nav;

        public XPathQuery() 
        {
            XPathDocument docNav = new XPathDocument("..\\..\\..\\TimetableXML.xml");
            nav = docNav.CreateNavigator();
        }

        private string getXPathResult(string xpath)
        {
            XPathNodeIterator NodeIter = nav.Select(xpath);
            string result = "XPath: " + xpath + "\n\n";
            foreach (XPathNavigator x in NodeIter)
            {
                result += x.OuterXml + "\n\n";
            }
            return result;
        }

        public string getAllLessons()
        {
            string xpath = "timetable/weekday/subject";
            return getXPathResult(xpath);
        }

        public string getAllRooms()
        {
            string xpath = "timetable/weekday/subject/room";
            return getXPathResult(xpath);
        }

        public string getAllPractice()
        {
            string xpath = "timetable/weekday/subject[@type='практика']";
            return getXPathResult(xpath);
        }

        public string getAllLessonsInRoom(string room)
        {
            string xpath = "timetable/weekday/subject[room='" + room + "']";
            return getXPathResult(xpath);
        }

        public string getAllProfessorsInRoom(string room)
        {
            string xpath = "timetable/weekday/subject[room='" + room + "']/professor";
            return getXPathResult(xpath);
        }

        public string getLastSubjects()
        {
            string xpath = "timetable/weekday/subject[last()]";
            return getXPathResult(xpath);
        }

        public string getLessonAmount()
        {
            string xpath = "count(timetable/weekday/subject)";
            string result = "XPath: " + xpath + "\n\n";
            result += "Общее количество занятий за всю неделю: " + nav.Evaluate(xpath);
            return result;
        }
    }
}
