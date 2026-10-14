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
                this.data[i][j] = data[i][j];
            }
        }
    }

        public int getRows () {
            return rows;
        }

        public int getCols () {
            return cols;
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
                "Количество столбцов первой матрицы "
                        + "должно совпадать с количеством строк второй"
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

}

