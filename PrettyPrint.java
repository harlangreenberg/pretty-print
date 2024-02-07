import java.util.*;
import java.io.*;

public class PrettyPrint {

    public static double calcSlack(int current_length, int line_length, SlackFunctor sf) {
        return sf.f(line_length - current_length);
    }

    public static List<Integer> splitWords(int[] lengths, int L, SlackFunctor sf) {

        int n = lengths.length;
        ArrayList<Double> slacks = new ArrayList<Double>(n+1);
        ArrayList<Integer> breaks = new ArrayList<Integer>(n+1);

        //Initialize slacks and breaks
        for (int i = 0; i <= n; i++) {
            slacks.add(Double.MAX_VALUE); //calculated slacks will be less than initial vals
            breaks.add(-1);
        }

        //Return if any of the words are longer than L
        for (int length : lengths) {
            if (length > L) {
                return null;
            }
        }

        slacks.set(0, 0.0);

        for (int i = 1; i <= n; i++) {
            int subLength = lengths[i-1];
            for (int j = i; j > 0; j--) {
                //no need for check if we know sublength is already greater than L
                if (subLength > L) {
                    break;
                }

                //calculate slack and penalty for current line
                double curr_slack = L - subLength;
                double penalty = sf.f((int)curr_slack) + slacks.get(j-1);

                //if new penalty is lower, update slacks and breaks
                if (penalty < slacks.get(i)) {
                    slacks.set(i, penalty);
                    breaks.set(i, j-1);
                }

                //update and cumulate the sublength
                if (j > 1) {
                    subLength += lengths[j-2] + 1;
                }
            }
        }


        //backtrack to return the correct breaks needed for the output
        LinkedList<Integer> output = new LinkedList<Integer>();
        int k = n;
        while (k > 0) {
            int wordBreak = breaks.get(k);
            //ensure no possible infinite loop
            if (wordBreak < 0 || wordBreak >= k) {
                throw new IllegalStateException("Invalid break point found");
            }
            output.addFirst(k - 1);
            k = wordBreak;
        }
        return output;
    }


    public static List<Integer> greedy_print(int[] lengths, int L, SlackFunctor sf) {

        /*  Greedy implementation
            provided in starter file

            It just places words on lines and inserts line breaks whenever the length
            would exceed L.
        */

        ArrayList<Integer> breaks = new ArrayList<Integer>();
        int current_length = 0;
        int current_end = -1;
        for (int word: lengths) {
            if (word > L) {
                return null;
            }

            if (current_length > 0 && current_length + 1 + word > L) {
                breaks.add(current_end);
                current_length = 0;
            }

            if (current_length > 0)
                current_length++;
            current_length += word;

            current_end++;
        }

        breaks.add(current_end);

        return breaks;
    }
    

    public static String help_message() {
        return
            "Usage: java PrettyPrint line_length [inputfile] [outputfile]\n" +
            "  line_length (required): an integer specifying the maximum length for a line\n" +
            "  inputfile (optional): a file from which to read the input text (stdin if a hyphen or omitted)\n" +
            "  outputfile (optional): a file in which to store the output text (stdout if omitted)"
            ;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: required argument line_length missing");
            System.err.println(help_message());
            System.exit(1);
            return;
        }

        int line_length;
        try {
            line_length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Error: could not find integer argument line_length");
            System.err.println(help_message());
            System.exit(2);
            return;
        }

        Scanner input = null;
        PrintStream output = null;
        if (args.length < 2) {
            input = new Scanner(System.in);
        }
        else if (args.length >= 2) {
            if (args[1].equals("-")) {
                input = new Scanner(System.in);
            } else {
                try {
                    input = new Scanner(new File(args[1]));
                } catch (FileNotFoundException e) {
                    System.err.println("Error: could not open input file");
                    System.err.println(help_message());
                    System.exit(3);
                    return;
                }
            }
        }

        if (args.length >= 3) {
            try {
                output = new PrintStream(args[2]);
            } catch (FileNotFoundException e) {
                System.err.println("Error: could not open output file");
                System.err.println(help_message());
                input.close();
                System.exit(4);
                return;
            }
        } else {
            output = System.out;
        }

        ArrayList<String> words = new ArrayList<String>();
        while (input.hasNext()) {
            words.add(input.next());
        }
        input.close();

        int[] lengths = new int[words.size()];
        for (int i = 0; i < words.size(); i++) {
            lengths[i] = words.get(i).length();
        }
        
        List<Integer> breaks = splitWords(lengths, line_length,
            new SlackFunctor() {
                public double f(int slack) { return slack * slack; }
            });

        System.out.println(breaks);

        if (breaks != null) {
            int current_word = 0;
            for (int next_break : breaks) {
                while (current_word < next_break) {
                    output.print(words.get(current_word++));
                    output.print(" ");
                }
                output.print(words.get(current_word++));
                output.println();
            }

            output.close();
            System.exit(0);
            return;
    
        } else {
            System.err.println("Error: formatting impossible; an input word exceeds line length");
            output.close();
            System.exit(5);
            return;
        }
    }
}
