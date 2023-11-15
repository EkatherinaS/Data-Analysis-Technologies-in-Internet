using Newtonsoft.Json.Linq;
using Newtonsoft.Json.Schema;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace Lab2
{
    internal class JSONXPathQuery : IXPathQuery
    {

        private JObject jsonObject;
        string path = Directory.GetParent(Environment.CurrentDirectory).Parent.Parent.FullName;

        public JSONXPathQuery()
        {
            string jsonText = File.ReadAllText(path + "\\TimetableJSON.json");
            jsonObject = JObject.Parse(jsonText);
        }

        public string getXPathResult(string xpath)
        {
            string result = "XPath: " + xpath + "\n\n";
            IEnumerable<JToken> jTokens = jsonObject.SelectTokens(xpath);
            foreach (JToken x in jTokens)
            {
                result += x.ToString() + "\n\n";
            }
            return result;
        }

        public string getAllLessons()
        {
            string xpath = "$.timetable.weekday[*].subject[*]";
            return getXPathResult(xpath);
        }

        public string getAllRooms()
        {
            string xpath = "$.timetable.weekday[*].subject[*].room";
            return getXPathResult(xpath);
        }

        public string getAllPractice()
        {
            string xpath = "$.timetable.weekday[*].subject[?(@.type=='практика')]";
            return getXPathResult(xpath);
        }

        public string getAllLessonsInRoom(string room)
        {
            string xpath = "$.timetable.weekday[*].subject[?(@.room=='" + room + "')]";
            return getXPathResult(xpath);
        }

        public string getAllProfessorsInRoom(string room)
        {
            string xpath = "$.timetable.weekday[*].subject[?(@.room=='" + room + "')].professor";
            return getXPathResult(xpath);
        }

        public string getLastSubjects()
        {
            string xpath = "$.timetable.weekday[*].subject[-1:]";
            return getXPathResult(xpath);
        }

        public string getLessonAmount()
        {
            string xpath = "$.timetable.weekday[*].subject[*]";
            string result = "XPath теоретически:\n$.length - не поддерживается в c#\n\n";
            result += "XPath практически:\n" + xpath + " с использованием метода count()\n\n";
            result += "Общее количество занятий за всю неделю: " + jsonObject.SelectTokens(xpath).Count();
            return result;
        }

        public bool validateBySchema()
        {
            string text = File.ReadAllText(path + "\\TimetableJSONSchema.json");
            JSchema schema = JSchema.Parse(text);
            return jsonObject.IsValid(schema);
        }
    }
}
