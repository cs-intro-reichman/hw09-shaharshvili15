import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        In file = new In(fileName);
        for (int i = 0; i < windowLength; i++)
            window += file.readChar();
        while (!file.isEmpty()) {
            char c = file.readChar();
            List charDataMap = this.CharDataMap.get(window);
            if (charDataMap == null) {
                charDataMap = new List();
                this.CharDataMap.put(window, charDataMap);
            }
            charDataMap.update(c);
            window = (window + c).substring(1);
        }
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        int allChars = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            allChars += probs.listIterator(i).current.cp.count;
        }
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentChar = probs.listIterator(i).current.cp;
            CharData previousChar = (i == 0) ? null : probs.listIterator(i - 1).current.cp;
            currentChar.p = (double) currentChar.count / allChars;
            if (i == 0) {
                currentChar.cp = currentChar.p;
            } else {
                currentChar.cp = currentChar.p + previousChar.cp;
            }
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        int count = 0 ;
        double r = randomGenerator.nextDouble();
        while (probs.listIterator(count).current.cp.cp<r){
            count++;
        }
        return probs.get(count).chr;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 *. @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
    public String generate(String initialText, int textLength) {
        // Check if initialText is at least windowLength characters long
        if (initialText.length() < windowLength) {
            throw new IllegalArgumentException("Initial text must be at least windowLength characters long");
        }
        StringBuilder generatedText = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);
        while (generatedText.length() < textLength + windowLength) {
            List charDataList = this.CharDataMap.get(window);
            if (charDataList == null) {
                break;
            }
            char selectedChar = getRandomChar(charDataList);
            generatedText.append(selectedChar);
            window = window.substring(1) + selectedChar;
        }
        return generatedText.toString();
    }

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 5) {
            System.out.println("Usage: java LanguageModelCLI <windowLength> <initialText> <generatedTextLength> <randomGeneration> <fileName>");
            return;
        }

        // Parse command-line arguments
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        boolean randomGeneration = args[3].equalsIgnoreCase("random");
        String fileName = args[4];

        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength);
        } else {
            lm = new LanguageModel(windowLength, 20); // Seed value of 20
        }

        // Trains the model, creating the map
        lm.train(fileName);

        // Generates text and prints it
        System.out.println(lm.generate(initialText, generatedTextLength));
    }

}
