package backpack;

import exceptions.AlgorithmRuntimeException;
import exceptions.IllegalTaskConditionException;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача:
    Есть N целых чисел, которые нужно разделить на M групп, так,
    чтобы в каждой группе сумма чисел была одинакова.

Коммментарий:
    Классическая NP-полная задача. Не шибко показательная, но какая есть.
    Слава богу, целые числа.
 */

public class NMBackpack {

    public static void main(String[] args) throws IllegalTaskConditionException{
        // Здесь будет запуск от строки, почему бы и нет.
        String c = "383 8 9 3 -17 3 3 4 2 1 -5 -2 -3 -380 0 0";
        int m = 3;
        List<Integer> a = Arrays.stream(c.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println(new NMBackpack().RecursiveBackpackSeparatorToMBackpacks(a, m));
//        System.out.println(new NMBackpack().BackpackSeparatorToMBackpacks(a, m));
    }
    public List<List<Integer>> RecursiveBackpackSeparatorToMBackpacks(final List<Integer> nor, int numberOfGroups) throws IllegalTaskConditionException {
        int totalSum = nor.stream().reduce(0, Integer::sum);
        if (nor.size() <= 0) {
            throw new IllegalTaskConditionException("Empty List!");
        }
        if (totalSum % numberOfGroups != 0) {
            throw new IllegalTaskConditionException("Total sum is " + totalSum + " , not div by " + numberOfGroups);
        }
        //Haha, why not
        if (totalSum == 0) {
            List<List<Integer>> groups = new ArrayList<>();
            groups.add(nor);
            for (int i = 1 ; i < numberOfGroups ; i++) groups.add(new ArrayList<>());
            return groups;
        }
        int groupSum = totalSum / numberOfGroups;
        int[] nors = new int[nor.size()];
        int[] groups = new int[nor.size()];
        for (int i = 0 ; i < nor.size() ; i++) {
            nors[i] = nor.get(i);
            groups[i] = 0;
        }

        List<List<Integer>> result = new LinkedList<>();
        if (
                recursiveNMBackpack(nors, groups,
                1, numberOfGroups,
                groupSum, 0)
        ) {
            for (int i = 0; i < numberOfGroups; i++) result.add(new LinkedList<>());
            for (int i = 0; i < nors.length; i++) {
                result.get(groups[i] - 1).add(nors[i]);
            }
        } else {
            throw new IllegalTaskConditionException("Тут даже полный перебор бессилен, невозможно разделить.");
        }


        return result;
    }

    private boolean recursiveNMBackpack(final int[] nor, final int[] groups,
                                        final int actualGroup, final int numberOfGroup,
                                        final int groupSum, final int actualSum) {
        if (actualGroup > numberOfGroup) {
            int[] tmpSum = new int[numberOfGroup];
            for (int i = 0 ; i < numberOfGroup ; i++) {
                tmpSum[i] = 0;
            }
            for (int i = 0 ; i < nor.length ; i++) {
                if (groups[i] <= 0) return false;
                tmpSum[groups[i]-1] += nor[i];
            }
            for (int i = 0 ; i < numberOfGroup ; i++) {
                if (tmpSum[i] != groupSum) return false;
            }
            return true;
        } else if (actualGroup > 0) {
            for (int i = 0 ; i < nor.length ; i++) {
                if (groups[i] <= 0) {
//               if (actualGroup > -groups[i]) {
                    groups[i] = actualGroup;
                    int nextSum   = actualSum + nor[i] == groupSum ? 0 : actualSum + nor[i];
                    int nextGroup = actualSum + nor[i] == groupSum ? actualGroup + 1 : actualGroup;
                    boolean answer = recursiveNMBackpack(nor, groups, nextGroup,numberOfGroup, groupSum, nextSum);
                    if (!answer) {
//                        groups[i] = -actualGroup;
                        groups[i] = 0;
                    } else {
                        return answer;
                    }
                }
            }
            return false;
        } else {
            throw new AlgorithmRuntimeException("Natural group numbers only!");
        }

    }

    public List<List<Integer>> BackpackSeparatorToMBackpacks(final List<Integer> nor, int numberOfGroups) throws IllegalTaskConditionException {
        int totalSum = nor.stream().reduce(0, Integer::sum);
        if (nor.size() <= 0) {
            throw new IllegalTaskConditionException("Empty List!");
        }
        if (totalSum % numberOfGroups != 0) {
            throw new IllegalTaskConditionException("Total sum is " + totalSum + " , not div by " + numberOfGroups );
        }
        List<List<Integer>> groups = new ArrayList<>();
        //Haha, why not
        if (totalSum == 0) {
            groups.add(nor);
            for (int i = 1 ; i < numberOfGroups; i++) groups.add(new ArrayList<>());
            return groups;
        }
        final List<Integer> a = new LinkedList<>(nor);
        Collections.sort(a);

        int groupSum = totalSum / numberOfGroups;



        class GroupStruct{
            int sum=0;
            Set<Integer> indexs = new TreeSet<>();

            public GroupStruct() {
            }

            @Override
            public String toString() {
                return "GroupStruct{" +
                        "sum=" + sum +
                        ", indexs=" + indexs +
                        '}';
            }

            public GroupStruct(GroupStruct gs) {
                this.sum = gs.sum;
                this.indexs = new TreeSet<>(gs.indexs);
            }
        }

        List<GroupStruct> gss = new ArrayList<>();
        gss.add(new GroupStruct());
        List<GroupStruct> totalgss = new ArrayList<>();

        for (int gi = 0; gi < gss.size(); gi++) {
            GroupStruct gs = gss.get(gi);
            boolean change = false;
            for (int i = 0; i < a.size(); i++) {
                if (gs.indexs.contains(i)) {
                    continue;
                }
                change = true;
                int next = a.get(i);
                GroupStruct gsn = new GroupStruct(gs);
                gsn.indexs.add(i);
                gsn.sum = gsn.sum + next;
                gss.add(gsn);
                if (gsn.sum == groupSum) {
                    totalgss.add(new GroupStruct(gsn));
                }
                gs = gss.get(gi);
            }
            if (!change) {
                gss.remove(gi);
            }
        }

        List<Set<Integer>> indx = retain(totalgss.stream().map( (x) -> x.indexs).collect(Collectors.toList()),
                numberOfGroups,
                null,
                nor.size());

        if (indx == null) throw new IllegalTaskConditionException("Тут даже полный перебор бессилен, невозможно разделить.");

        groups = indx.stream().map(LinkedList::new).collect(Collectors.toList());
        System.out.println(groups);
        System.out.println(a);
        groups = groups.stream().map( (x) -> x.stream().map(a::get).collect(Collectors.toList())).collect(Collectors.toList());
        System.out.println(groups);

        return groups;
    }

    private List<Set<Integer>> retain (final List<Set<Integer>> groups,
                                       final int numberOfGroups,
                                       final List<Integer> indexUsedGroup,
                                       final int norSize) throws IllegalTaskConditionException {
        if (indexUsedGroup == null || indexUsedGroup.size() == 0) {
            for (int i = 0; i < groups.size(); i++) {
                List<Integer> choosen = new LinkedList<>();
                choosen.add(i);
                List<Set<Integer>> result = retain(groups, numberOfGroups,choosen,norSize);
                if (result != null) return result;
            }
            return null;
        } else
        if (indexUsedGroup.size() < numberOfGroups) {
            for (int i = indexUsedGroup.get(indexUsedGroup.size() - 1) ; i < groups.size(); i++) {
                List<Integer> choosen = new LinkedList<>(indexUsedGroup);
                choosen.add(i);
                List<Set<Integer>> result = retain(groups, numberOfGroups,choosen, norSize);
                if (result != null) return result;
            }
            return null;
        } else if (indexUsedGroup.size() == numberOfGroups) {
            Set<Integer> total = new HashSet<>();
            List<Set<Integer>> result = new LinkedList<>();
            for (int i = 0; i < indexUsedGroup.size() ; i++ ) {
                Set<Integer> check = groups.get(indexUsedGroup.get(i));
                if (!total.addAll(check)) {
                    return null;
                }
                result.add(check);
            }
            if (total.size() == norSize) {
                return result;
            } else {
                return null;
            }
        }
        return null;
    }

}
