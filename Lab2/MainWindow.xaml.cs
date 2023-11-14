﻿using System.IO;
using System.Windows;
using System.Xml.Xsl;
using System;
using System.Xml;
using System.Xml.XPath;
using System.Windows.Controls;

namespace Lab2
{
    public partial class MainWindow : Window
    {
        string path = Directory.GetParent(Environment.CurrentDirectory).Parent.Parent.FullName;
        XPathQuery xPathQuery;


        public static void CreateResultFile(string initialFile)
        {
            string text = File.ReadAllText(initialFile);
            WriteToResultFile(text);
        }
        public static void WriteToResultFile(string text, string path = "result.txt")
        {
            File.WriteAllText(path, text);
        }


        public MainWindow()
        {
            InitializeComponent(); 
            xPathQuery = new XPathQuery();
            CreateResultFile(path + "\\TimetableXML.xml");
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXML_Click(object sender, RoutedEventArgs e)
        {
            CreateResultFile(path + "\\TimetableXML.xml");
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnDTD_Click(object sender, RoutedEventArgs e)
        {
            CreateResultFile(path + "\\TimetableDTD.dtd");
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXMLSchema_Click(object sender, RoutedEventArgs e)
        {
            CreateResultFile(path + "\\TimetableXMLSchema.xsd");
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath1_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getAllLessons();
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath2_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getAllRooms();
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath3_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getAllPractice();
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath4_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getAllLessonsInRoom("дист.[0]");
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath5_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getAllProfessorsInRoom("дист.[0]");
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath6_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getLastSubjects();
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXPath7_Click(object sender, RoutedEventArgs e)
        {
            string result = xPathQuery.getLessonAmount();
            WriteToResultFile(result);
            wbResult.Navigate("file://" + path + "\\bin/Debug/net6.0-windows/result.txt");
        }

        private void btnXSLTToTXT_Click(object sender, RoutedEventArgs e)
        {
            XmlDocument xml = new XmlDocument();
            xml.Load(path + "\\TimetableXML.xml");

            XmlDocument stylesheet = new XmlDocument();
            stylesheet.Load(path + "\\TimetableXSLTToTXT.xslt");

            XslCompiledTransform transform = new XslCompiledTransform();
            transform.Load(stylesheet);
            StringWriter writer = new StringWriter();
            XmlReader xmlReadB = new XmlTextReader(new StringReader(xml.DocumentElement.OuterXml));
            transform.Transform(xmlReadB, null, writer);

            WriteToResultFile(writer.ToString(), path + "\\TimetableTXT.txt");

            wbResult.Navigate("file://" + path + "\\TimetableTXT.txt");
        }

        private void btnXSLTToHTML_Click(object sender, RoutedEventArgs e)
        {
            XmlDocument xml = new XmlDocument();
            xml.Load(path + "\\TimetableXML.xml");

            XmlDocument stylesheet = new XmlDocument();
            stylesheet.Load(path + "\\TimetableXSLTToHTML.xslt");

            XslCompiledTransform transform = new XslCompiledTransform();
            transform.Load(stylesheet);
            StringWriter writer = new StringWriter();
            XmlReader xmlReadB = new XmlTextReader(new StringReader(xml.DocumentElement.OuterXml));
            transform.Transform(xmlReadB, null, writer);

            WriteToResultFile(writer.ToString(), path + "\\TimetableHTML.html");

            wbResult.Navigate("file://" + path + "\\TimetableHTML.html");
        }
    }
}
