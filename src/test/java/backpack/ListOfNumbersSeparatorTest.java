package backpack;

import static backpack.ListOfNumbersSeparator.splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import exceptions.IllegalTaskConditionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.RepeatedTest;

class ListOfNumbersSeparatorTest {

  @org.junit.jupiter.api.Test
  void splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearchTestBase() {
    String strOfNumbers = "383 8 9 3 -17 3 3 4 2 1 -5 -2 -3 -380 0 -3 -3 -3 0";
    int numberOfGroups = 3;
    List<Integer> listOfNumbers = Arrays.stream(strOfNumbers.split(" "))
        .map(Integer::parseInt)
        .collect(toList());

    try {
      List<List<Integer>> result = splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
          listOfNumbers, numberOfGroups);

      for (List<Integer> group : result) {
        for (Integer number : group) {
          final int indexOfNumber = listOfNumbers.indexOf(number);
          if (indexOfNumber == -1) {
            fail("Extra numbers in groups");
          }
          listOfNumbers.remove(indexOfNumber);
        }
      }
      assertTrue(listOfNumbers.isEmpty());
    } catch (IllegalTaskConditionException e) {
      fail(e.getMessage());
    }

  }


  @org.junit.jupiter.api.Test
  void splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearchTestIllegalTaskConditions() {
    List<Integer> listOfNumbers = new LinkedList<>();
    final int numberOfGroups = 4;

    IllegalTaskConditionException thrownEmptyList = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, numberOfGroups),
        "Expected Empty list exception"
    );

    assertTrue(thrownEmptyList.getMessage().contains("Empty list of numbers"));

    listOfNumbers.add(1);
    listOfNumbers.add(2);
    listOfNumbers.add(3);
    listOfNumbers.add(4);

    IllegalTaskConditionException thrownIncorrectNumberOfGroup = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, 0),
        "Expected Illegal group number"
    );

    assertTrue(thrownIncorrectNumberOfGroup.getMessage().contains("Illegal number of Groups"));

    IllegalTaskConditionException thrownIncorrectSumOfNumbers = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, numberOfGroups),
        "Expected Illegal group number"
    );

    assertTrue(thrownIncorrectSumOfNumbers.getMessage().contains("Total sumOfNumbersInGroup"));
  }

  @RepeatedTest(20)
  void splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearchTestRandomList() {
    Random rand = new Random(new Date().getTime());

    final int numberOfGroups = rand.nextInt(10)+1;
    final int targetSumOfGroup = rand.nextInt(10) - 5;

    List<Integer> listOfNumbers = new ArrayList<>();

    for (int groupNumber = 0; groupNumber < numberOfGroups; groupNumber++) {
      final int groupSize = rand.nextInt(10) + 1;
      int sumOfGroup = 0;
      for (int groupMemberNumber = 0; groupMemberNumber < groupSize - 1; groupMemberNumber++) {
        final int member = rand.nextInt(100) - 50;
        listOfNumbers.add(member);
        sumOfGroup += member;
      }
      listOfNumbers.add(targetSumOfGroup - sumOfGroup);
    }

    try {
      List<List<Integer>> result = splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
          listOfNumbers, numberOfGroups);

      for (List<Integer> group : result) {
        for (Integer number : group) {
          final int indexOfNumber = listOfNumbers.indexOf(number);
          if (indexOfNumber == -1) {
            fail("Extra numbers in groups");
          }
          listOfNumbers.remove(indexOfNumber);
        }
      }
      assertTrue(listOfNumbers.isEmpty());
    } catch (IllegalTaskConditionException e) {
      fail(e.getMessage());
    }

  }


  @org.junit.jupiter.api.Test
  void splitListOfNumbersIntoGroupsOfEqualSumByGroupCombinationTestBase() {
    String strOfNumbers = "3 3 3 -2 -1 3 0 -1 -1 -1 -2 3 2";
    int numberOfGroups = 3;
    List<Integer> listOfNumbers = Arrays.stream(strOfNumbers.split(" "))
        .map(Integer::parseInt)
        .collect(toList());

    try {
      List<List<Integer>> result = splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
          listOfNumbers, numberOfGroups);

      for (List<Integer> group : result) {
        for (Integer number : group) {
          final int indexOfNumber = listOfNumbers.indexOf(number);
          if (indexOfNumber == -1) {
            fail("Extra numbers in groups");
          }
          listOfNumbers.remove(indexOfNumber);
        }
      }
      assertTrue(listOfNumbers.isEmpty());
    } catch (IllegalTaskConditionException e) {
      fail(e.getMessage());
    }

  }


  @org.junit.jupiter.api.Test
  void splitListOfNumbersIntoGroupsOfEqualSumByGroupCombinationTestIllegalTaskConditions() {
    List<Integer> listOfNumbers = new LinkedList<>();
    final int numberOfGroups = 4;

    IllegalTaskConditionException thrownEmptyList = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, numberOfGroups),
        "Expected Empty list exception"
    );

    assertTrue(thrownEmptyList.getMessage().contains("Empty list of numbers"));

    listOfNumbers.add(1);
    listOfNumbers.add(2);
    listOfNumbers.add(3);
    listOfNumbers.add(4);

    IllegalTaskConditionException thrownIncorrectNumberOfGroup = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, 0),
        "Expected Illegal group number"
    );

    assertTrue(thrownIncorrectNumberOfGroup.getMessage().contains("Illegal number of Groups"));

    IllegalTaskConditionException thrownIncorrectSumOfNumbers = assertThrows(
        IllegalTaskConditionException.class,
        () -> splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
            listOfNumbers, numberOfGroups),
        "Expected Illegal group number"
    );

    assertTrue(thrownIncorrectSumOfNumbers.getMessage().contains("Total sumOfNumbersInGroup"));
  }

  @RepeatedTest(20)
  void splitListOfNumbersIntoGroupsOfEqualSumByGroupCombinationTestRandomList() {
    Random rand = new Random(new Date().getTime());

    final int numberOfGroups = rand.nextInt(3)+1;
    final int targetSumOfGroup = rand.nextInt(10) - 5;

    List<Integer> listOfNumbers = new ArrayList<>();

    for (int groupNumber = 0; groupNumber < numberOfGroups; groupNumber++) {
      final int groupSize = rand.nextInt(3) + 1;
      int sumOfGroup = 0;
      for (int groupMemberNumber = 0; groupMemberNumber < groupSize - 1; groupMemberNumber++) {
        final int member = rand.nextInt(100) - 50;
        listOfNumbers.add(member);
        sumOfGroup += member;
      }
      listOfNumbers.add(targetSumOfGroup - sumOfGroup);
    }

    try {
      List<List<Integer>> result = splitListOfNumbersIntoGroupsOfEqualSumUsingDeepSearch(
          listOfNumbers, numberOfGroups);

      for (List<Integer> group : result) {
        for (Integer number : group) {
          final int indexOfNumber = listOfNumbers.indexOf(number);
          if (indexOfNumber == -1) {
            fail("Extra numbers in groups");
          }
          listOfNumbers.remove(indexOfNumber);
        }
      }
      assertTrue(listOfNumbers.isEmpty());
    } catch (IllegalTaskConditionException e) {
      fail(e.getMessage());
    }

  }
}