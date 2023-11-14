using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab1
{
    internal class TextDictionary
    {
        //словоформа, количество_вхождений_словоформы с сортировкой по количеству вхождений. 
        Dictionary<string, int> Dictionary { get; }

        public TextDictionary() { }

        public override string ToString()
        {
            return Dictionary.ToString();
        }
    }
}
