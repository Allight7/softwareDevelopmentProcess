package ru.ifmo.sdp.matrix;

/**
 * User: allight
 * Date: 02.10.2015 16:48
 */
public final class Matrix {
    private final int rows;                 //число строк
    private final int columns;              //число столбцов
    private double[][] data;                //хранимые значения

    private static double equalsDelta = 0;

    public static enum Operation {
        SUM, SUBTRACT, MULTIPLY, DIVIDE
    }

    public Matrix(int rows) {
        if (rows < 1)
            throw new IllegalArgumentException("Entered vector length is less then one.");
        this.rows = rows;
        this.columns = 1;
        this.data = new double[rows][this.columns];
    }

    Matrix(int rows, int y) {
        if (rows < 1 || y < 1)
            throw new IllegalArgumentException("One of entered matrix sizes is less then one.");
        this.rows = rows;
        this.columns = y;
        this.data = new double[rows][y];
    }

    Matrix(double[][] data) {
        if (data == null || data.length < 1 || data[0].length < 1)
            throw new IllegalArgumentException("One of sizes in imported data is less then one.");
        int yCheck = data[0].length;            // В java разрешены в т.ч. треугольные двумерные массивы
        for (double[] subData : data)           // Проверяем, что аргумент имеет одинаковую длину внутренних массивов
            if (subData == null || subData.length != yCheck)
                throw new IllegalArgumentException("One of sizes in imported data is less then one.");
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            this.data[i] = data[i].clone();
    }

    Matrix(Matrix toClone) {
        this(toClone.getDataClone());
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public double[][] getDataClone() {
        double[][] dataClone = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            dataClone[i] = data[i].clone();
        return dataClone;
    }

    public double getElement(int row, int column) {
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException("Wrong row index. Expected index value between 0 and " +
                    rows + " (excluded).");
        if (column < 0 || column >= columns)
            throw new IndexOutOfBoundsException("Wrong column index. Expected index value between 0 and " +
                    columns + " (excluded).");
        return data[row][column];
    }

    public double getElement(int row) {
        if (columns != 1)
            throw new IllegalStateException("Method only defined for vectors or matrices with 1 column");
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException("Wrong row index. Expected index value between 0 and " +
                    rows + " (excluded).");
        return data[row][0];
    }

    /**
     * Устанавливает заданое значение по указанным индексам матрицы, возвращая страрое значение
     *
     * @param row    индекс строки
     * @param column индекс столбца
     * @param value  устанавливаемое значение
     * @return предыдущее значение (@code value)
     */
    public double setElement(int row, int column, double value) {
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException("Wrong row index. Expected index value between 0 and " +
                    rows + " (excluded).");
        if (column < 0 || column >= columns)
            throw new IndexOutOfBoundsException("Wrong column index. Expected index value between 0 and " +
                    columns + " (excluded).");
        double result = data[row][column];
        data[row][column] = value;
        return result;
    }

    public double setElement(int row, double value) {
        if (columns != 1)
            throw new IllegalStateException("Method only defined for vectors or matrices with 1 column");
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException("Wrong row index. Expected index value between 0 and " +
                    rows + " (excluded).");
        double result = data[row][0];
        data[row][0] = value;
        return result;
    }

    public static Matrix matrixElementWiseOperation(Matrix m1, Matrix m2, Operation operation) {
        if (m1 == null || m2 == null)
            throw new IllegalArgumentException("Expected NotNull arguments.");
        if (m1.rows() != m2.rows() || m1.columns() != m2.columns())
            throw new IllegalArgumentException("Matrices have different sizes.");
        double[][] data = m1.getDataClone();
        switch (operation) {
            case SUM:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] += m2.getElement(i, j);
                break;
            case SUBTRACT:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] -= m2.getElement(i, j);
                break;
            case MULTIPLY:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] *= m2.getElement(i, j);
                break;
            case DIVIDE:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] /= m2.getElement(i, j);
                break;
        }
        return new Matrix(data);
    }

    public Matrix addMatrix(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.SUM);
    }

    public Matrix subMatrix(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.SUBTRACT);
    }

    public Matrix mulMatrixElementWise(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.MULTIPLY);
    }

    public Matrix divMatrixElementWise(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.DIVIDE);
    }

    public static Matrix matrixMultiply(Matrix m1, Matrix m2) {
        if (m1 == null || m2 == null)
            throw new IllegalArgumentException("Expected NotNull arguments.");
        if (m1.columns() != m2.rows())
            throw new IllegalArgumentException("First matrix should have rowNum equal to second matrix colNum.");
        int newRow = m1.rows();
        int newCol = m2.columns();
        int common = m2.rows();
        double[][] data = new double[newRow][newCol];
        for (int i = 0; i < newRow; i++)
            for (int j = 0; j < newCol; j++)
                for (int k = 0; k < common; k++)
                    data[i][j] += m1.getElement(i, k) * m2.getElement(k, j);
        return new Matrix(data);
    }

    public Matrix mulMatrix(Matrix m) {
        if (m == null)
            throw new IllegalArgumentException("Expected NotNull argument.");
        if (this.columns() != m.rows())
            throw new IllegalArgumentException("Entered matrix should have rowNum equal to base matrix colNum.");
        return Matrix.matrixMultiply(this, m);
    }

    public static Matrix scalarOperation(Matrix m1, double operand, Operation operation) {
        if (m1 == null)
            throw new IllegalArgumentException("Expected NotNull arguments.");
        double[][] data = m1.getDataClone();
        switch (operation) {
            case SUM:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] += operand;
                break;
            case SUBTRACT:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] -= operand;
                break;
            case MULTIPLY:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] *= operand;
                break;
            case DIVIDE:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] /= operand;
                break;
        }
        return new Matrix(data);
    }

    public Matrix addScalar(double operand) {
        return Matrix.scalarOperation(this, operand, Operation.SUM);
    }

    public Matrix subScalar(double operand) {
        return Matrix.scalarOperation(this, operand, Operation.SUBTRACT);
    }

    public Matrix mulScalar(double operand) {
        return Matrix.scalarOperation(this, operand, Operation.MULTIPLY);
    }

    public Matrix divScalar(double operand) {
        return Matrix.scalarOperation(this, operand, Operation.DIVIDE);
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject)
            return true;
        if (anObject instanceof Matrix) {
            Matrix matrix = (Matrix) anObject;
            if (this.rows == matrix.rows && this.columns == matrix.columns) {
                for (int i = 0; i < rows; i++)
                    for (int j = 0; j < columns; j++)
                        if (Math.abs(this.data[i][j] - matrix.data[i][j]) > equalsDelta)
                            return false;
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Matrix transpose() {
        int newRows = this.columns;
        int newColumns = this.rows;
        double[][] resData = new double[newRows][newColumns];
        for (int i = 0; i < newRows; ++i) {
            for (int j = 0; j < newColumns; ++j) {
                resData[i][j] = this.data[j][i];
            }
        }
        return new Matrix(resData);
    }
}
