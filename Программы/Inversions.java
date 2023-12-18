import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Inversions {

    static long inversionAmount = 0;

    /* Слияние двух массивов.
    Тут объединяются два упорядоченных массива в один. Пришлось использовать два дополнительных массива:
    leftArray и rightArray для временного хранения левой и правой частей. Они сливаются в изначальный массив array.
    */
    static void merge(ArrayList<Integer> array, int start, int middle, int finish) {
        int leftSize = middle - start + 1;
        int rightSize = finish - middle;
        ArrayList<Integer> leftArray = new ArrayList<>(); // Массив элементов слева от центра.
        ArrayList<Integer> rightArray = new ArrayList<>(); // Массив элементов справа от центра.
        // Вносим нужные данные из основного массива в новые массивы.
        // leftIndex - индекс элемента в массиве чисел слева от центра (leftArray).
        // rightIndex - индекс элемента в массиве чисел справа от центра (rigthArray).
        for (int leftIndex = 0; leftIndex < leftSize; ++leftIndex) leftArray.add(array.get(start + leftIndex));
        for (int rightIndex = 0; rightIndex < rightSize; ++rightIndex) rightArray.add(array.get(middle + 1 + rightIndex));
        int leftIndex = 0, rightIndex = 0;
        int mergedIndex = start; // индекс текущего элемента в основном массиве array, куда всё объединяется.
        // Объединение полученных массивов в единое целое.
        while (leftIndex < leftSize && rightIndex < rightSize) {
            // Если элемент из массива слева меньше или равен элементу из массива чисел справа, то элемент из
            // массива чисел слева от центра переходит в основной массив, иначе - из массива чисел справа от центра.
            if (leftArray.get(leftIndex) <= rightArray.get(rightIndex)) {
                array.set(mergedIndex, leftArray.get(leftIndex));
                leftIndex++;
            } else {
                array.set(mergedIndex, rightArray.get(rightIndex));
                rightIndex++;
                inversionAmount += leftSize - leftIndex;
            }
            mergedIndex++;
        }
        // Ещё есть элементы в массиве чисел слева от центра? Добавляем их в основной массив.
        while (leftIndex < leftSize) {
            array.set(mergedIndex, leftArray.get(leftIndex));
            leftIndex++;
            mergedIndex++;
        }
        // Аналогично, если ещё есть элементы в массиве чисел справа от центра, то и их добавляем в основной массив.
        while (rightIndex < rightSize) {
            array.set(mergedIndex, rightArray.get(rightIndex));
            rightIndex++;
            mergedIndex++;
        }
    }


    /* Сортировка слиянием, время работы = O (n * log(n)).
    Тут происходит разделение массива на две части (От start до middle и от middle до finish), которые
    сортируются по отдельности, после чего их объединяет функция merge.
    Повторяется, пока весь массив не будет отсортирован.
     */
    static void mergeSort(ArrayList<Integer> array, int start, int finish) {
        if (start < finish) {
            int middle = (start + finish) / 2;
            mergeSort(array, start, middle);
            mergeSort(array, middle + 1, finish);
            merge(array, start, middle, finish);
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int needed = 100000;
        StringBuilder results = new StringBuilder();
        while (needed < 500000) {
            results.append(needed).append(";");
            ArrayList<Integer> array = new ArrayList<>();
            inversionAmount = 0;
            for (int i = 0; i < needed; i++) array.add(rand.nextInt(1001));
            long startTime = System.nanoTime();
            mergeSort(array, 0, array.size() - 1);
            long endTime = System.nanoTime();
            long spent_time = endTime-startTime; // Divide by 1000000 to get milliseconds.
            results.append(spent_time / 1000000).append(",").append((spent_time / 10000) % 100).append("\n");
            needed += 1000;
        }
        File outputFile = new File("Inversion Results.txt");
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(results.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
