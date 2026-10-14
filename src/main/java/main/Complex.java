package main;

public final class Complex {

    final double real;
    final double imaginary;

    private static final double threshold = 1e-10; // чтоб избежать проблем неточности double'ов при проверке на ноль

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex add(Complex other) {
        return new Complex(
                real + other.real,
                imaginary + other.imaginary
        );
    }

    public Complex subtract(Complex other) {
        return new Complex(
                real - other.real,
                imaginary - other.imaginary
        );
    }

    public Complex multiply(Complex other) {
        double newReal =
                real * other.real - imaginary * other.imaginary;

        double newImaginary =
                real * other.imaginary + imaginary * other.real;

        return new Complex(newReal, newImaginary);
    }

    public Complex divide(Complex divisor) { // по формуле: (a1 + b1i) / (a2 + b2i) = (a1a2 + b1b2) / (a2² + b2²) +
                                                                                // + (b1a2 - a1b2) / (a2² + b2²) * i

        if (divisor.real == 0 && divisor.imaginary == 0) {
            throw new ArithmeticException(
                    "Деление комплексного числа на ноль"
            );
        }

        double denominator =
                divisor.real * divisor.real
                        + divisor.imaginary * divisor.imaginary;

        double newReal =
                (this.real * divisor.real
                        + this.imaginary * divisor.imaginary) / denominator;

        double newImaginary =
                (this.imaginary * divisor.real
                        - this.real * divisor.imaginary) / denominator;

        return new Complex(newReal, newImaginary);

    }

    public double abs() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public boolean isZero() {
        return abs() <= threshold;
    }

    @Override
    public String toString() {
        if (isZero()) {
            return "0";
        }

        if (Math.abs(imaginary) <= threshold) {
            return format(real);
        }

        if (Math.abs(real) <= threshold) {
            return format(imaginary);
        }

        String sign = imaginary >= 0 ? " + " : " - ";

        return format(real) + sign
                + format(Math.abs(imaginary)) + "i";
    }

    private static String format(double value) { // для человекочитаемого вывода
        if (Math.abs(value) <= threshold) {
            value = 0;
        }

        if (value == Math.rint(value)) // отбрасываем дробную часть
                 {
            return Long.toString((long) value);
        }

        return Double.toString(value);
    }
}
