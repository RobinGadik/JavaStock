package backpack;

import exceptions.AlgorithmRuntimeException;
import exceptions.IllegalTaskConditionException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        String c = "1 2 3 -6 7 -8 4";
        int m = 3;
        List<Integer> a = Arrays.stream(c.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        System.out.println(new NMBackpack().BackpackSeparatorToMBackpacks(a, m));
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
