package main;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in); // для более простого ввода комплексных чисел

    private static Matrix currentMatrix;
    private static Complex currentComplexResult;
    //private static Complex currentDeterminant; // кэшируем детерминант
    private static boolean currentMatrixFlag = false;

    public static void main(String[] args) {
        System.out.println("=== Калькулятор матриц ===");

        while (true) {
            printCurrentResult();
            printMenu();

            int choice = readInt("Ваш выбор: ");

            try {
                switch (choice) {
                    case 1 -> enterMatrix();
                    case 2 -> add();
                    case 3 -> subtract();
                    case 4 -> multiplyByMatrix();
                    case 5 -> multiplyByNumber();
                    case 6 -> divide();
                    case 7 -> transpose();
                    case 8 -> inverse();
                    case 9 -> determinant();
                    case 10 -> printCurrentResult();
                    case 11 -> clearResult();
                    case 0 -> {
                        System.out.println("Выход из программы");
                        return;
                    }
                    default -> System.out.println("Нет такого пункта меню");
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("""
                
                Выберите операцию:
                1. Ввод новой матрицы
                2. Сложение
                3. Вычитание
                4. Умножение матриц
                5. Умножение на число
                6. Деление матриц
                7. Транспонирование
                8. Обратная матрица
                9. Определитель
                10. Показать текущий результат
                11. Очистить результат
                0. Выход
                """);
    }

    private static void printCurrentResult() {
        if (currentMatrixFlag) {
            System.out.println("Текущий результат:");
            currentMatrix.prettyPrint();
        } else if (currentComplexResult != null) {
            System.out.println(
                    "Текущий результат: " + currentComplexResult
            );
        } else {
            System.out.println("Текущий результат не задан.");
        }
    }

    private static void enterMatrix() {
        currentMatrix = readMatrix("Введите матрицу:");
        currentComplexResult = null;
        currentMatrixFlag = true;

        System.out.println("Матрица сохранена как текущий результат.");
        currentMatrix.prettyPrint();
    }

    
private static void add() {
    requireCurrentMatrix();

    Matrix other = readMatrix("Введите вторую матрицу:");
    saveMatrixResult(currentMatrix.add(other));
}

private static void subtract() {
    requireCurrentMatrix();

    Matrix other = readMatrix("Введите вторую матрицу:");
    saveMatrixResult(currentMatrix.subtract(other));
}

private static void multiplyByMatrix() {
    requireCurrentMatrix();

    Matrix other = readMatrix("Введите вторую матрицу:");
    saveMatrixResult(currentMatrix.multiply(other));
}

private static void divide() {
    requireCurrentMatrix();

    Matrix other = readMatrix("Введите матрицу-делитель:");
    saveMatrixResult(currentMatrix.divide(other));
}


    private static void multiplyByNumber() {
        requireCurrentMatrix();

        System.out.println("""
                Выберите тип числа:
                1. Вещественное
                2. Комплексное
                """);

        int type = readInt("Ваш выбор: ");
        Matrix result;

        if (type == 1) {
            double number = readDouble("Введите вещественное число: ");
            result = currentMatrix.multiply(number);
        } else if (type == 2) {
            Complex number = readComplex(
                    "Введите комплексное число: "
            );
            result = currentMatrix.multiply(number);
        } else {
            System.out.println("Нет такого пункта");
            return;
        }

        saveMatrixResult(result);
    }

    private static void transpose() {
        requireCurrentMatrix();
        saveMatrixResult(currentMatrix.transpose());
    }

    private static void inverse() {
        requireCurrentMatrix();
        saveMatrixResult(currentMatrix.inverseMatrix());
    }

    private static void determinant() {
        requireCurrentMatrix();

        currentComplexResult = currentMatrix.determinant();
        currentMatrixFlag = false;

        System.out.println(
                "Определитель: " + currentComplexResult
        );
    }

    private static void saveMatrixResult(Matrix result) {
        currentMatrix = result;
        currentComplexResult = null;
        currentMatrixFlag = true;

        System.out.println("Результат сохранён как текущий");
        currentMatrix.prettyPrint();
    }

    private static void clearResult() {
        currentMatrix = null;
        currentComplexResult = null;
        currentMatrixFlag = false;

        System.out.println("Текущий результат очищен");
    }

    private static void requireCurrentMatrix() { // проверка на то что матрица для операции вообще существует
        if (!currentMatrixFlag || currentMatrix == null) {
            throw new IllegalArgumentException(
                    "Сначала введите матрицу или получите матричный результат"
            );
        }
    }

    private static Matrix readMatrix(String message) { // самописный input матрицы
        System.out.println(message);

        int rows = readPositiveInt("Количество строк: ");
        int cols = readPositiveInt("Количество столбцов: ");

        Complex[][] data = new Complex[rows][cols];

        System.out.println(
                "Введите элементы построчно, без пробелов между знаками. Если у числа нет реальной или мнимой части - не пишите 0; Примеры комплексного числа: 2+3i, 2-3i, 5, -i"
        );

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = readComplex(
                        "Элемент [" + (i + 1) + "][" + (j + 1) + "]: "
                );
            }
        }

        return new Matrix(data);
    }

    private static Complex readComplex(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.next();

            try {
                return parseComplex(input);
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Некорректное число. Примеры: 3, 2+3i, 2-3i, -i"
                );
            }
        }
    }

    private static Complex parseComplex(String input) {
        String value = input.replace(" ", "");

        if (!value.endsWith("i")) {
            return new Complex(Double.parseDouble(value), 0);
        }

        String imaginaryPart = value.substring(0, value.length() - 1);

        // Числа i, -i и +i
        if (imaginaryPart.isEmpty() || imaginaryPart.equals("+")) {
            imaginaryPart = "1";
        } else if (imaginaryPart.equals("-")) {
            imaginaryPart = "-1";
        }

        // Ищем знак между вещественной и мнимой частями
        int separator = -1;

        for (int i = 1; i < imaginaryPart.length(); i++) {
            char c = imaginaryPart.charAt(i);

            if (c == '+' || c == '-') {
                separator = i;
            }
        }

        if (separator == -1) {
            return new Complex(
                    0,
                    Double.parseDouble(imaginaryPart)
            );
        }

        double real = Double.parseDouble(
                imaginaryPart.substring(0, separator)
        );

        double imaginary = Double.parseDouble(
                imaginaryPart.substring(separator)
        );

        return new Complex(real, imaginary);
    }

    private static int readPositiveInt(String message) {
        while (true) {
            int value = readInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Число должно быть положительным.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Введите вещественное число.");
            }
        }
    }
}
