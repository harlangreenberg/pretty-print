import java.util.*;

public class Similarity {

    public static <E> Map<Integer, Map<Integer, Double>>
        jaccard(ArrayList<Set<E>> setList) {

        HashMap<Integer, Map<Integer, Double>> R = new HashMap<Integer, Map<Integer, Double>>();

            // your code goes here

        if (setList == null) {  //Exception for when input list is null
            return null;
        }

        
        //Create a HashMap of all unqiue elements amongst all sets. Elements within each set are a list of the indices of the sets in input list.
        HashMap<E, ArrayList<Integer>> Z = new HashMap<E, ArrayList<Integer>>();
        for (int index = 0; index < setList.size(); index++) {
            Set<E> set = setList.get(index);
            for (E elem : set) {
                if (Z.containsKey(elem)) {      //append to end of list if element is a key
                    Z.get(elem).add(index);
                }
                else {                          //create a new set if element is not a key
                    ArrayList<Integer> arrayOfSets = new ArrayList<Integer>();
                    arrayOfSets.add(index);
                    Z.put(elem, arrayOfSets);

                }
            }
            
        }


        //Creating a new HashMap that is to be returned.
        for (int index = 0; index < setList.size(); index++) {  //iterate through the sets in input list
            Map<Integer, Double> subMap = new HashMap<Integer, Double>();
            Set<E> set = setList.get(index);
            '''
            for (E elem : set) {
                ArrayList<Integer> arrayOfSets = Z.get(elem);
                for (int arrayIndex : arrayOfSets) {         //compare the similarity between sets. Build jaccard variable values based on comparisons. 
                    if (arrayIndex > index) {
                        Set<E> set_compare = setList.get(arrayIndex);
                        double numerator = 0;
                        double denominator = set.size() + set_compare.size();
                        double jaccard;
                        for (E elem_compare : set_compare) {
                            if (set.contains(elem_compare)) {
                                numerator += 1;
                                denominator -= 1;
                            }
                        }
                        if (numerator != 0 && denominator != 0) {  //only create jaccard similarity if it is non-zero
                            jaccard = numerator/denominator;
                            subMap.put(arrayIndex, jaccard);
                        }
                    }
                }
            }
'''
            if (subMap.size() > 0) {  //only add to HashMap if a set has similarity with any others
                R.put(index, subMap);
            }

        }

        return R;
    }

    public static void main(String[] args) {

        ArrayList<Set<Integer>> setList = new ArrayList<Set<Integer>>();
        for (int i = 0; i < 10000; i+=2) {
            HashSet<Integer> set1 = new HashSet<Integer>();
            HashSet<Integer> set2 = new HashSet<Integer>();
            set1.add(i * 10);
            set2.add(i * 10);
            for (int j = 1; j < 5; j++) {
                set1.add(i*10+j);
                set2.add(i*10+j+5);
            }
            setList.add(set1);
            setList.add(set2);
        }

        System.out.println(jaccard(setList));

    }
    
}

