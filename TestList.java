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
        LanguageModel languageModel = new LanguageModel(7);
        languageModel.calculateProbabilities(list);
        var iterator = list.listIterator(0);
        while (iterator.hasNext()){
            System.out.println("Character: " + iterator.current.cp.chr + ", Probability: " + iterator.current.cp.p +
                    ", Cumulative Probability: " + iterator.current.cp.cp);
            iterator.next();
        }
        Random random = new Random();
        int numberOfTests = 10; // Number of random character tests
        for (int i = 0; i < numberOfTests; i++) {
            char randomChar = languageModel.getRandomChar(list);
            System.out.println("Random Character " + (i+1) + ": " + randomChar);
        }
    }
}
