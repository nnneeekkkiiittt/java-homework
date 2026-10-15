package main;

public class Matrix {
    private final Complex[][] data;
    private final int rows;
    private final int cols;

    public Matrix(Complex[][] data) {
        if (data == null || data.length == 0 || data[0] == null
                || data[0].length == 0) {
            throw new IllegalArgumentException(
                    "Пустая матрица"
            );
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i] == null || data[i].length != cols) {
                throw new IllegalArgumentException(
                        "Матрица НЕ прямоугольная"
                );
            }

            for (int j = 0; j < cols; j++) {
                if (data[i][j] == null) {
            throw new IllegalArgumentException(
                "Элемент матрицы не может быть null"
            );
        }
                this.data[i][j] = data[i][j];
            }
        }
    }


    public Matrix add(Matrix other) {
    if (rows != other.rows || cols != other.cols) {
        throw new IllegalArgumentException(
                "Размер матриц отличается"
        );
    }

    Complex[][] res = new Complex[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            res[i][j] = data[i][j].add(other.data[i][j]);
        }
    }

    return new Matrix(res);
}

    public Matrix subtract(Matrix other) {
    if (rows != other.rows || cols != other.cols) {
        throw new IllegalArgumentException(
                "Размер матриц отличается"
        );
    }

    Complex[][] result = new Complex[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[i][j] = data[i][j].subtract(other.data[i][j]);
        }
    }

    return new Matrix(result);
}

    public Matrix multiply(double number) { // умножение на вещественное число
    Complex[][] result = new Complex[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[i][j] = data[i][j].multiply(
                    new Complex(number, 0)
            );
        }
    }

    return new Matrix(result);
}

    public Matrix multiply(Complex number) { // умножение на комплексное число
    Complex[][] result = new Complex[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[i][j] = data[i][j].multiply(number);
        }
    }

    return new Matrix(result);
}



    public Matrix multiply(Matrix other) { // перемножение матриц
    if (cols != other.rows) {
        throw new IllegalArgumentException(
            "Размер матриц не подходит для перемножения"
        );
    }

    Complex[][] result = new Complex[rows][other.cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < other.cols; j++) {
            Complex sum = new Complex(0, 0);

            for (int k = 0; k < cols; k++) {
                sum = sum.add(
                    data[i][k].multiply(other.data[k][j])
                );
            }

            result[i][j] = sum;
        }
    }
    return new Matrix(result);
}

private Matrix minorForDeterminant(int row, int col) { // вычеркиваем одну строку и столбец - для разложения по Лапласу
    Complex[][] res = new Complex[rows - 1][cols - 1];

    int newRow = 0;

    for (int i = 0; i < rows; i++) {
        if (i == row) {
            continue;
        }

        int newCol = 0;

        for (int j = 0; j < cols; j++) {
            if (j == col) {
                continue;
            }

            res[newRow][newCol] = data[i][j];
            newCol++;
        }

        newRow++;
    }

    return new Matrix(res);
}

public Complex determinant() { // считаем рекурсивно по Лапласу
    if (rows != cols) {
        throw new IllegalArgumentException(
        "Матрица не квадратна; невозможно посчитать определитель");
    }

    if (rows == 1) {
        return data[0][0];
    }

    Complex det = new Complex(0, 0);

    for (int j = 0; j < cols; j++) {
        Complex smallDet = minorForDeterminant(0, j).determinant().multiply(new Complex(j % 2 == 1 ? -1 : 1, 0));

        det = det.add(data[0][j].multiply(smallDet));
    }

    return det;
}

public Matrix transpose() { // транспонирование
    Complex[][] res = new Complex[cols][rows];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            res[j][i] = data[i][j];
        }
    }

    return new Matrix(res);
}


public Matrix inverseMatrix() {
    if (rows != cols) {
        throw new IllegalArgumentException(
        "Матрица не квадратна; невозможно найти обратную");
    }

    if (rows == 1) { // корнер кейс 1x1
    Complex value = new Complex(1, 0).divide(data[0][0]);

    return new Matrix(new Complex[][]{{value}});
    }

    Complex det = determinant();

    if (det.isZero()) {
        throw new ArithmeticException(
            "Матрица вырождена"
        );
    }

    Complex[][] factors = new Complex[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            Complex value = minorForDeterminant(i, j).determinant().multiply(new Complex((i + j) % 2 == 1 ? -1 : 1, 0));
            factors[i][j] = value;
        }
    }

    Matrix adjugateMatrix = new Matrix(factors).transpose(); // adjugate = союзная

    return adjugateMatrix.multiply(new Complex(1, 0).divide(det)); // 1 / det
}

public Matrix divide(Matrix divisor) {
    // делить можно если
    // 1) делитель квадратный
    // 2) делитель невырожден
    // 3) матрицы можно перемножить - это проверится уже в методе multiply
    if (divisor.rows != divisor.cols) {
        throw new IllegalArgumentException(
            "Делитель должен быть квадратной матрицей"
        );
    }

    return multiply(divisor.inverseMatrix());
}

    public void prettyPrint() {
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (j > 0) {
                System.out.print("\t");
            }

            System.out.print(data[i][j]);
        }

        System.out.println();
    }
}

}

