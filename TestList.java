import java.util.Random;

public class TestList {

    public static void main(String[] args) {
        var list = new List();
        list.update('_');
        list.update('e');
        list.update('e');
        list.update('t');
        list.update('t');
        list.update('i');
        list.update('m');
        list.update('m');
        list.update('o');
        list.update('c');
        LanguageModel languageModel = new LanguageModel(4);
        languageModel.calculateProbabilities(list);
        var iterator = list.listIterator(0);
        while (iterator.hasNext()){
            System.out.println("Character: " + iterator.current.cp.chr + ", Probability: " + iterator.current.cp.p +
                    ", Cumulative Probability: " + iterator.current.cp.cp);
            iterator.next();
        }
        languageModel.train("test.txt");
        String initialText1 = "you_";
        int textLength1 = 20;
        String generatedText1 = languageModel.generate(initialText1, textLength1);
        System.out.println("Generated Text 1:");
        System.out.println(generatedText1);

        //String initialText2 = "Hello, ";
        //int textLength2 = 20;
        //String generatedText2 = languageModel.generate(initialText2, textLength2);
        //System.out.println("Generated Text 2:");
        //System.out.println(generatedText2);


    }
}
