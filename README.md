# Projects for DESRES viewing



Here is the highlighted section of the Similarity.java file:

            Set<E> set = setList.get(index);
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
