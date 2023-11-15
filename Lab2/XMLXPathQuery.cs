using Microsoft.SqlServer.Management.Sdk.Sfc;
using System;
using System.Configuration;
using System.IO;
using System.Xml.Schema;
using System.Xml.XPath;
using System;
using System.IO;
using System.Text;
using System.Xml;
using System.Xml.Schema;

namespace Lab2
{
    public class XMLXPathQuery : IXPathQuery
    {
        XPathNavigator nav;

        public XMLXPathQuery() 
        {
            XPathDocument docNav = new XPathDocument("..\\..\\..\\TimetableXML.xml");
            nav = docNav.CreateNavigator();
        }

        public string getXPathResult(string xpath)
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

        public bool validateBySchema()
        {
            string path = Directory.GetParent(Environment.CurrentDirectory).Parent.Parent.FullName;
            XmlSchemaSet schemaSet = new XmlSchemaSet();
            schemaSet.Add(null, path + "\\TimetableXMLSchema.xsd");
            try
            {
                return nav.CheckValidity(schemaSet, null);
            }
            catch
            {
                return false;
            }
        }
    }
}
