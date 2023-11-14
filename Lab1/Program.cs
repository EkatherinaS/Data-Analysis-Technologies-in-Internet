using Lab1;


TextFile.ChangeWorkingDirectoryToProject();

TextFile inputFile = new("input.txt");
TextFile outputFile = new("output.txt");
inputFile.Add("completly new text");
inputFile.Add("new text");
Console.WriteLine(inputFile.Read());

