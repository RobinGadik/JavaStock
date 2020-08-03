package backpack;

import exceptions.AlgorithmRuntimeException;
import exceptions.IllegalTaskConditionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Задача: Есть N целых чисел, которые нужно разделить на M групп, так, чтобы в каждой группе сумма
 * чисел была одинакова. Коммментарий: Классическая NP-полная задача. Не шибко показательная, но
 * какая есть. Слава богу, целые числа. Если существует алгоритм её решения полимиального - пишите.
 */
public class ListOfNumbersSeparator {

  /**
   * Запуск алгоритма решения задачи от строки.
   *
   * @param args - не используется
   * @throws IllegalTaskConditionException - если условие некорретно.
   */
  public static void main(String[] args) throws IllegalTaskConditionException {
    String c = "383 8 9 3 -17 3 3 4 2 1 -5 -2 -3 -380 0 -3 -3 -3 0";
    int m = 3;
    List<Integer> a = Arrays.stream(c.split(" "))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    System.out.println(SplitListOfNumbersIntoGroupsOfEqualSumUsingRecursiveAlgorithm(a, m));
  }


  /**
   * Рекурсивная реализация алгоритма. Приводит данные у удобному типу для {@link
   * ListOfNumbersSeparator#assignGroupNumberToArrayOfNumbersByDeepSearch(int[], int[], int, int,
   * int, int)}
   *
   * @return - Список групп(списков чисел)
   * @throws IllegalTaskConditionException - Выбрасывается, если разделить на группы равной суммы
   * невозможно.
   */
  public static List<List<Integer>> SplitListOfNumbersIntoGroupsOfEqualSumUsingRecursiveAlgorithm(
      final List<Integer> listOfNumbers,
      final int numberOfGroups) throws IllegalTaskConditionException {
    final int sumOfAllNumbers = listOfNumbers.stream().reduce(0, Integer::sum);
    if (listOfNumbers.size() <= 0) {
      throw new IllegalTaskConditionException("Empty list of numbers");
    }
    if (sumOfAllNumbers % numberOfGroups != 0) {
      throw new IllegalTaskConditionException(
          "Total sumOfNumbersInGroup is " + sumOfAllNumbers + " , not div by " + numberOfGroups);
    }

    final int groupSum = sumOfAllNumbers / numberOfGroups;

    int[] arrayOfNumbers = new int[listOfNumbers.size()];
    int[] arrayOfNumbersGroupIndexs = new int[listOfNumbers.size()];

    for (int i = 0; i < listOfNumbers.size(); i++) {
      arrayOfNumbers[i] = listOfNumbers.get(i);
      arrayOfNumbersGroupIndexs[i] = 0;
    }

    List<List<Integer>> result = new LinkedList<>();
    if (assignGroupNumberToArrayOfNumbersByDeepSearch(arrayOfNumbers, arrayOfNumbersGroupIndexs,
        1, numberOfGroups,
        groupSum, 0)) {
      for (int i = 0; i < numberOfGroups; i++) {
        result.add(new LinkedList<>());
      }
      for (int i = 0; i < arrayOfNumbers.length; i++) {
        result.get(arrayOfNumbersGroupIndexs[i] - 1).add(arrayOfNumbers[i]);
      }
    } else {
      throw new IllegalTaskConditionException(
          "Тут даже полный перебор бессилен, невозможно разделить.");
    }

    return result;
  }

  /**
   * Рекурсивная реализация алгоритма. Тупой полный перебор поиском в глубину. Банально и
   * безвкусно.
   * <p>
   *
   * @param arrayOfNumbers - массив исходных чисел
   * @param arrayOfNumbersGroupIndexs - массив индексов групп для каждого числа
   * @param actualGroupNumber - номер текущей вычисляемой группы
   * @param numberOfGroup - Количество групп, на которые делится массив.
   * @param targetGroupSum - сумма чисел в каждой группе, которой надо добиться.
   * @param actualGroupSum - Сумма чисел текущей группы.
   * @return True, для успешного разделения, False иначе.
   */
  private static boolean assignGroupNumberToArrayOfNumbersByDeepSearch(final int[] arrayOfNumbers,
      final int[] arrayOfNumbersGroupIndexs,
      final int actualGroupNumber, final int numberOfGroup,
      final int targetGroupSum, final int actualGroupSum) {
    if (actualGroupNumber > numberOfGroup) {
      int[] tmpSum = new int[numberOfGroup];
      for (int i = 0; i < numberOfGroup; i++) {
        tmpSum[i] = 0;
      }
      for (int i = 0; i < arrayOfNumbers.length; i++) {
        if (arrayOfNumbersGroupIndexs[i] <= 0) {
          return false;
        }
        tmpSum[arrayOfNumbersGroupIndexs[i] - 1] += arrayOfNumbers[i];
      }
      for (int i = 0; i < numberOfGroup; i++) {
        if (tmpSum[i] != targetGroupSum) {
          return false;
        }
      }
      return true;
    } else if (actualGroupNumber > 0) {
      for (int i = 0; i < arrayOfNumbers.length; i++) {
        if (arrayOfNumbersGroupIndexs[i] <= 0) {
          arrayOfNumbersGroupIndexs[i] = actualGroupNumber;
          int nextSum = actualGroupSum + arrayOfNumbers[i] == targetGroupSum ? 0
              : actualGroupSum + arrayOfNumbers[i];
          int nextGroup =
              actualGroupSum + arrayOfNumbers[i] == targetGroupSum ? actualGroupNumber + 1
                  : actualGroupNumber;
          boolean answer = assignGroupNumberToArrayOfNumbersByDeepSearch(
              arrayOfNumbers, arrayOfNumbersGroupIndexs, nextGroup, numberOfGroup, targetGroupSum,
              nextSum);
          if (!answer) {
            arrayOfNumbersGroupIndexs[i] = 0;
          } else {
            return true;
          }
        }
      }
      return false;
    } else {
      throw new AlgorithmRuntimeException("Natural group numbers only!");
    }
  }

  /**
   * @return Список групп(списков чисел)
   * @throws IllegalTaskConditionException -  - Выбрасывается, если разделить на группы равной суммы
   * невозможно.
   * @deprecated (вызываеи java heap space при большом количестве чисел) Алгоритм итеративно строит
   * список всевозможных групп с необходимой суммой. (@link {@link ListOfNumbersSeparator#selectFromListOfGroupCombinationOfNonOverlappingGroupsWithTotalUnion(List,
   * int, List, int)}} выбирает из них подходящую комбинацию. После чего востанавливаются по
   * нидексам чисел группы
   */
  @Deprecated
  public static List<List<Integer>> SplitListOfNumbersIntoGroupsOfEqualSumUsingAlgorithmForCombinationOfGroups(
      final List<Integer> listOfNumbers,
      final int numberOfGroups) throws IllegalTaskConditionException {

    final int sumOfAllNumbers = listOfNumbers.stream().reduce(0, Integer::sum);
    if (listOfNumbers.size() <= 0) {
      throw new IllegalTaskConditionException("Empty list of numbers");
    }
    if (sumOfAllNumbers % numberOfGroups != 0) {
      throw new IllegalTaskConditionException(
          "Total sumOfNumbersInGroup is " + sumOfAllNumbers + " , not div by " + numberOfGroups);
    }

    final int groupSum = sumOfAllNumbers / numberOfGroups;

    class GroupOfNumbers {

      private int sumOfNumbersInGroup = 0;
      private Set<Integer> indexsOfNumbersInGroupFromListOfNumbers = new TreeSet<>();

      GroupOfNumbers() {
      }

      @Override
      public String toString() {
        return "GroupOfNumbers{" +
            "sumOfNumbersInGroup=" + sumOfNumbersInGroup +
            ", indexsOfNumbersInGroupFromListOfNumbers=" + indexsOfNumbersInGroupFromListOfNumbers +
            '}';
      }

      GroupOfNumbers(GroupOfNumbers gs) {
        this.sumOfNumbersInGroup = gs.sumOfNumbersInGroup;
        this.indexsOfNumbersInGroupFromListOfNumbers = new TreeSet<>(
            gs.indexsOfNumbersInGroupFromListOfNumbers);
      }
    }

    List<GroupOfNumbers> gss = new ArrayList<>();
    gss.add(new GroupOfNumbers());
    List<GroupOfNumbers> totalgss = new ArrayList<>();

    for (int gi = 0; gi < gss.size(); gi++) {
      GroupOfNumbers group = gss.get(gi);
      boolean flagOfListChange = false;
      for (int i = 0; i < listOfNumbers.size(); i++) {
        if (group.indexsOfNumbersInGroupFromListOfNumbers.contains(i)) {
          continue;
        }
        flagOfListChange = true;
        final int nextNumber = listOfNumbers.get(i);
        GroupOfNumbers extendedGroup = new GroupOfNumbers(group);
        extendedGroup.indexsOfNumbersInGroupFromListOfNumbers.add(i);
        extendedGroup.sumOfNumbersInGroup = extendedGroup.sumOfNumbersInGroup + nextNumber;
        gss.add(extendedGroup);
        if (groupSum == extendedGroup.sumOfNumbersInGroup) {
          totalgss.add(new GroupOfNumbers(extendedGroup));
        }
        group = gss.get(gi);
      }
      if (!flagOfListChange) {
        gss.remove(gi);
      }
    }

    Optional<List<Set<Integer>>> indx = selectFromListOfGroupCombinationOfNonOverlappingGroupsWithTotalUnion(
        totalgss.stream().map((x) -> x.indexsOfNumbersInGroupFromListOfNumbers)
            .collect(Collectors.toList()),
        numberOfGroups,
        null,
        listOfNumbers.size());

    if (indx.isEmpty()) {
      throw new IllegalTaskConditionException(
          "Невозможно разделить на " + numberOfGroups + " групп с суммой элементов " + groupSum);
    }

    List<List<Integer>> listOfGroups = indx.get().stream().map(LinkedList::new)
        .collect(Collectors.toList());
    listOfGroups = listOfGroups.stream()
        .map((x) -> x.stream()
            .map(listOfNumbers::get)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());

    return listOfGroups;
  }

  /**
   * Рекунсивно выбирает список непересекающихся групп, При обхеденении которых получается список
   * чисел от 0 до countOfElementsInUnion (Количество исходных чисел)
   *
   * @param listOfGroups Список групп, множеств с индексами используемых чисел.
   * @param numberOfGroups - количество выбираемых групп.
   * @param listOfIndexsOfUsedGroup - индексы использованных групп
   * @param countOfElementsInUnion - количество исходных чисел
   * @return - Список групп (списков индексов среди исходных чисел)
   */
  private static Optional<List<Set<Integer>>> selectFromListOfGroupCombinationOfNonOverlappingGroupsWithTotalUnion(
      final List<Set<Integer>> listOfGroups,
      final int numberOfGroups,
      final List<Integer> listOfIndexsOfUsedGroup,
      final int countOfElementsInUnion) {
    if (listOfIndexsOfUsedGroup == null || listOfIndexsOfUsedGroup.size() == 0) {
      for (int i = 0; i < listOfGroups.size(); i++) {
        List<Integer> choosen = new LinkedList<>();
        choosen.add(i);
        Optional<List<Set<Integer>>> result = selectFromListOfGroupCombinationOfNonOverlappingGroupsWithTotalUnion(
            listOfGroups, numberOfGroups, choosen, countOfElementsInUnion);
        if (result.isPresent()) {
          return result;
        }
      }
      return Optional.empty();
    } else if (listOfIndexsOfUsedGroup.size() < numberOfGroups) {
      for (int i = listOfIndexsOfUsedGroup.get(listOfIndexsOfUsedGroup.size() - 1);
          i < listOfGroups.size(); i++) {
        List<Integer> choosen = new LinkedList<>(listOfIndexsOfUsedGroup);
        choosen.add(i);
        Optional<List<Set<Integer>>> result = selectFromListOfGroupCombinationOfNonOverlappingGroupsWithTotalUnion(
            listOfGroups, numberOfGroups, choosen, countOfElementsInUnion);
        if (result.isPresent()) {
          return result;
        }
      }
      return Optional.empty();
    } else if (numberOfGroups == listOfIndexsOfUsedGroup.size()) {
      Set<Integer> total = new HashSet<>();
      List<Set<Integer>> result = new LinkedList<>();
      for (Integer integer : listOfIndexsOfUsedGroup) {
        Set<Integer> check = listOfGroups.get(integer);
        if (!total.addAll(check)) {
          return Optional.empty();
        }
        result.add(check);
      }
      if (countOfElementsInUnion == total.size()) {
        return Optional.of(result);
      } else {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

}
