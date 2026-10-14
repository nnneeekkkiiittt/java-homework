package main;

public class Matrix {
    private final Complex[][] data;
    private final int rows;
    private final int cols;

    public Matrix(Complex[][] data) {
        if (data == null || data.length == 0 || data[0] == null
                || data[0].length == 0) {
            throw new IllegalArgumentException(
                    "Матрица не должна быть пустой"
            );
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i] == null || data[i].length != cols) {
                throw new IllegalArgumentException(
                        "Все строки матрицы должны иметь одинаковую длину"
                );
            }

            for (int j = 0; j < cols; j++) {
                if (data[i][j] == null) {
                    throw new IllegalArgumentException(
                            "Элементы матрицы не должны быть null"
                    );
                }

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
    }

