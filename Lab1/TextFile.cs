using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Net.Mime.MediaTypeNames;

namespace Lab1
{
    internal class TextFile
    {
        public string Path { get; set; }
        public string File { get; set; }


        public static void ChangeWorkingDirectoryToProject()
        {
            string projectPath = Directory.GetParent(Environment.CurrentDirectory).Parent.Parent.FullName;
            Environment.CurrentDirectory = projectPath;
        }


        public TextFile(string path)
        {
            this.Path = path;
            FileStream fs = new (path, FileMode.OpenOrCreate);
            fs.Close();
        }

        public string Read() {
            string text = "";
            using (StreamReader reader = new StreamReader(Path))
            {
                text = reader.ReadToEnd();
            }
            return text;
        }
        public async void Write(string text) {
            using (StreamWriter writer = new StreamWriter(Path, false))
            {
                await writer.WriteLineAsync(text);
            }
        }

        public async void Add(string text)
        {
            using (StreamWriter writer = new StreamWriter(Path, true))
            {
                await writer.WriteLineAsync(text);
            }
        }
    }
}
