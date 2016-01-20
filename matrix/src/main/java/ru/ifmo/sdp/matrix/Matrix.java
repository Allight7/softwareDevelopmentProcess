package ru.ifmo.sdp.matrix;

/**
 * User: allight
 * Date: 02.10.2015 16:48
 */
public final class Matrix {
    private final int rows;                 // число строк. неименно
    private final int columns;              // число столбцов. неизнно
    private double[][] data;                // хранимые значения

    private static double equalsDelta = 0;  // допустимая разница при сравнении чисел

    /**
     * Допустимые операции (в java нет перегрузки операторов)
     */
    public enum Operation {
        SUM, SUBTRACT, MULTIPLY, DIVIDE
    }

    /**
     * Базовый конструктор. Созданная матрица заполняется значениями по-умолчанию,
     * для <code>double</code> это - <code>0.0</code>. Минимальный размер матрицы
     * - 1х1
     *
     * @param rows    число строк создаваемой матрицы. Неизменно после создания
     * @param columns число столбцов. Неизменно после создания
     * @throws IllegalArgumentException если один из переданных размеров матрицы
     *                                  меньше единицы
     */
    Matrix(int rows, int columns) {
        if (rows < 1 || columns < 1)
            throw new IllegalArgumentException(
                    "One of entered matrix sizes is less then one.");
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    /**
     * Конструктор вектора. Отличается возможностью не указывать значение второго
     * измерения для стандартного типа векторов - векторов-столбцов
     *
     * @param rows число строк создаваемого вектора. Неизменно после создания
     * @throws IllegalArgumentException если переданный размер вектора
     *                                  меньше единицы
     */
    public Matrix(int rows) {
        if (rows < 1)
            throw new IllegalArgumentException(
                    "Entered vector length is less then one.");
        this.rows = rows;
        this.columns = 1;
        this.data = new double[rows][this.columns];
    }

    /**
     * Конструктор на основе массива данных
     *
     * @param data двумерный массив данных. Длина как основного, так и вложенных
     *             массивов должна быть больше <code>0</code>. Вложенные массивы
     *             должны иметь одинаковую длину
     * @throws NullPointerException     если один из массивов равен <code>null</code>
     * @throws IllegalArgumentException если длина одного из массивов меньше
     *                                  единицы или если часть вложенных массивов
     *                                  имеют разную длину
     */
    Matrix(double[][] data) {
        if (data == null || data.length < 1 || data[0].length < 1)
            throw new IllegalArgumentException(
                    "One of sizes in imported data is less then one.");
        /**
         * В java разрешены в т.ч. треугольные двумерные массивы
         * Проверяем, что аргумент имеет одинаковую длину внутренних массивов
         */
        int yCheck = data[0].length;
        for (double[] subData : data)
            if (subData == null || subData.length != yCheck)
                throw new IllegalArgumentException(
                        "All inner arrays should have equal sizes.");

        this.rows = data.length;
        this.columns = data[0].length;
        this.data = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            this.data[i] = data[i].clone();
    }

    /**
     * Конструктор копирования
     *
     * @param toClone непустая ссылка на экземпляр класса <code>Matrix</code>
     * @throws NullPointerException аргумент равен <code>null</code>
     */
    Matrix(Matrix toClone) {
        this(toClone.getDataClone());
    }

    /**
     * Метод для получения текущего значения дельты - допустимой разницы
     * при сравнении чисел
     *
     * @return текущеее значение дельты
     */
    public static double equalsDelta() {
        return equalsDelta;
    }

    /**
     * Метод для задания значения дельты - допустимой разницы при сравнении чисел
     *
     * @param equalsDelta новое значение дельты
     * @throws IllegalArgumentException если аргумент меньше <code>0</code>
     */
    public static void setEqualsDelta(double equalsDelta) {
        if (equalsDelta < 0)
            throw new IllegalArgumentException("Expected positive value");
        Matrix.equalsDelta = equalsDelta;
    }

    /**
     * @return количество строк матрицы (первого измерения)
     */
    public int rows() {
        return rows;
    }

    /**
     * @return количество столбцов матрицы (второго измерения)
     */
    public int columns() {
        return columns;
    }

    /**
     * @return копия массива данных матрицы
     */
    public double[][] getDataClone() {
        double[][] dataClone = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            dataClone[i] = data[i].clone();
        return dataClone;
    }

    /**
     * Получение зачения элемента матрицы по его индексу
     *
     * @param row    индекс строки <code>0:rows-1</code>
     * @param column индекс столбца <code>0:column-1</code>
     * @return зачение элемента матрицы
     * @throws IndexOutOfBoundsException если индекс меньше <code>0</code> или
     *                                   больше соотв границы
     */
    public double getElement(int row, int column) {
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException(
                    "Wrong row index. Expected index value between 0 and " +
                            rows + " (excluded).");
        if (column < 0 || column >= columns)
            throw new IndexOutOfBoundsException(
                    "Wrong column index. Expected index value between 0 and " +
                            columns + " (excluded).");
        return data[row][column];
    }

    /**
     * Получение зачения элемента вектора по его индексу. Отличается возможностью
     * не указывать значение второго измерения для стандартного типа векторов -
     * векторов-столбцов
     *
     * @param row индекс строки <code>0:rows-1</code>
     * @return зачение элемента матрицы
     * @throws IllegalStateException     при вызове от матрицы с неск столбцами
     * @throws IndexOutOfBoundsException если индекс меньше <code>0</code> или
     *                                   больше длины вектора
     */
    public double getElement(int row) {
        if (columns != 1) throw new IllegalStateException(
                "Method only defined for vectors or matrices with 1 column");
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException(
                    "Wrong row index. Expected index value between 0 and " +
                            rows + " (excluded).");
        return data[row][0];
    }

    /**
     * Устанавливает заданое значение по указанным индексам матрицы, возвращая
     * предыдущее значение
     *
     * @param row    индекс строки
     * @param column индекс столбца
     * @param value  устанавливаемое значение
     * @return предыдущее значение <code>value</code>
     * @throws IndexOutOfBoundsException если индекс меньше <code>0</code> или
     *                                   больше соотв границы
     */
    public double setElement(int row, int column, double value) {
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException(
                    "Wrong row index. Expected index value between 0 and " +
                            rows + " (excluded).");
        if (column < 0 || column >= columns)
            throw new IndexOutOfBoundsException(
                    "Wrong column index. Expected index value between 0 and " +
                            columns + " (excluded).");

        double result = data[row][column];
        data[row][column] = value;
        return result;
    }

    /**
     * Устанавливает заданое значение по указанным индексам матрицы, возвращая
     * предыдущее значение. Отличается возможностью не указывать значение
     * второго измерения для стандартного типа векторов - векторов-столбцов
     *
     * @param row   индекс строки
     * @param value устанавливаемое значение
     * @return предыдущее значение <code>value</code>
     * @throws IllegalStateException     при вызове от матрицы с неск столбцами
     * @throws IndexOutOfBoundsException если индекс меньше <code>0</code> или
     *                                   больше соотв границы
     */
    public double setElement(int row, double value) {
        if (columns != 1)
            throw new IllegalStateException(
                    "Method only defined for vectors or matrices with 1 column");
        if (row < 0 || row >= rows)
            throw new IndexOutOfBoundsException(
                    "Wrong row index. Expected index value between 0 and " +
                            rows + " (excluded).");

        double result = data[row][0];
        data[row][0] = value;
        return result;
    }

    /**
     * Поэлементные операции над матрицами. Включает сложение, вычетание,
     * умножение и деление
     *
     * @param m1        Первая матрица
     * @param m2        Вторая матрица, совпадающая по размерам с первой
     * @param operation Одна из четырех доступных операций. Например,
     *                  <code>Operation.MULTIPLY</code>
     * @return новая матрица, являющаяся результатом операции
     * @throws IllegalArgumentException если одна из матриц равна <code>null</code>,
     *                                  если матрицы не совпадают по размерам
     *                                  или если во второй матрице присутствуют
     *                                  нулевые элементы и выбрана операция деления
     */
    public static Matrix matrixElementWiseOperation(Matrix m1, Matrix m2,
                                                    Operation operation) {
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
                        if (m2.getElement(i, j) != 0)
                            data[i][j] /= m2.getElement(i, j);
                        else throw new IllegalArgumentException(
                                "Second matrix shouldn't contain zero elements" +
                                        " for division operation");
                break;
        }
        return new Matrix(data);
    }

    /**
     * Нестатический метод поэлементного сложения с матрицей
     *
     * @param m матрица, второй операнд операции
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix addMatrix(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.SUM);
    }

    /**
     * Нестатический метод поэлементного вычитания матрицей
     *
     * @param m матрица, второй операнд операции
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix subMatrix(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.SUBTRACT);
    }

    /**
     * Нестатический метод поэлементного умножения на матрицу
     *
     * @param m матрица, второй операнд операции
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix mulMatrixElementWise(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.MULTIPLY);
    }

    /**
     * Нестатический метод поэлементного деления на матрицу
     *
     * @param m матрица, второй операнд операции
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix divMatrixElementWise(Matrix m) {
        return Matrix.matrixElementWiseOperation(this, m, Operation.DIVIDE);
    }

    /**
     * Статический метод перемножения матриц
     *
     * @param m1 первая матрица
     * @param m2 вторая матрица, число строк в которой совпадает с числом
     *           столбцов в первой
     * @return новая матрица, являющаяся результатом операции
     * @throws IllegalArgumentException если одна из матриц равна <code>null</code>
     *                                  или если число столбцов в первой не совпадает
     *                                  с числом строк во второй
     */
    public static Matrix matrixMultiply(Matrix m1, Matrix m2) {
        if (m1 == null || m2 == null)
            throw new IllegalArgumentException("Expected NotNull arguments.");
        if (m1.columns() != m2.rows())
            throw new IllegalArgumentException(
                    "First matrix should have rowNum equal to second matrix colNum.");
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

    /**
     * Нестатический метод перемножения матриц
     *
     * @param m второй операнд умножения - матрица, число строк в которой
     *          совпадает с числом столбцов в вызывающей
     * @return новая матрица, являющаяся результатом операции
     * @throws IllegalArgumentException если матрица равна <code>null</code>
     *                                  или если число её строк не совпадает
     *                                  с числом столбцов в исходной
     */
    public Matrix mulMatrix(Matrix m) {
        if (m == null)
            throw new IllegalArgumentException("Expected NotNull argument.");
        if (this.columns() != m.rows())
            throw new IllegalArgumentException(
                    "Entered matrix should have rowNum equal to base matrix colNum.");
        return Matrix.matrixMultiply(this, m);
    }

    /**
     * Статический метод для выполения скалярных операций над матрицами.
     * Включает сложение, вычетание, умножение и деление на скалярное число
     *
     * @param m         Матрица
     * @param scalar    Скалярное число
     * @param operation Одна из четырех доступных операций
     *                  Например, <code>Operation.MULTIPLY</code>
     * @return новая матрица, являющаяся результатом операции
     * @throws IllegalArgumentException если матрица равна <code>null</code>
     *                                  или скаляр равен нулю и выбрано деление
     */
    public static Matrix scalarOperation(Matrix m, double scalar, Operation operation) {
        if (m == null)
            throw new IllegalArgumentException("Expected NotNull arguments.");
        double[][] data = m.getDataClone();
        switch (operation) {
            case SUM:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] += scalar;
                break;
            case SUBTRACT:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] -= scalar;
                break;
            case MULTIPLY:
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] *= scalar;
                break;
            case DIVIDE:
                if (scalar == 0)
                    throw new IllegalArgumentException(
                            "Scalar shouldn't be a zero for division operation");
                for (int i = 0; i < data.length; i++)
                    for (int j = 0; j < data[0].length; j++)
                        data[i][j] /= scalar;
                break;
        }
        return new Matrix(data);
    }

    /**
     * Нестатический метод сложения матрицы со скалярным числом
     *
     * @param scalar Скалярное число
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix addScalar(double scalar) {
        return Matrix.scalarOperation(this, scalar, Operation.SUM);
    }

    /**
     * Нестатический метод вычитания из матрицы скалярного числа
     *
     * @param scalar скалярное число
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix subScalar(double scalar) {
        return Matrix.scalarOperation(this, scalar, Operation.SUBTRACT);
    }

    /**
     * Нестатический метод умножения матрицы на скалярное число
     *
     * @param scalar скалярное число
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix mulScalar(double scalar) {
        return Matrix.scalarOperation(this, scalar, Operation.MULTIPLY);
    }

    /**
     * Нестатический метод деления матрицы на скалярное число
     *
     * @param scalar скалярное число
     * @return новая матрица, являющаяся результатом операции
     */
    public Matrix divScalar(double scalar) {
        return Matrix.scalarOperation(this, scalar, Operation.DIVIDE);
    }

    /**
     * Сравнивает матрицу на идентичность с заданным объектом. Результатом
     * будет <code>true</code> тогда и только тогда, когда когда аргументом
     * не равен <code>null</code>, является объектом класса <code>Matrix</code>
     * и представляет тот же набор данных, что и вызывающий объект
     *
     * @param anObject объект, с которым нужно сравнить эту <code>Matrix</code>
     * @return <code>true</code>, если переданный объект представляет собой
     * <code>Matrix</code> с тем же набором данных, что и вызывающий объект
     * @throws NullPointerException если объект равен <code>null</code>
     */
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

    /**
     * Возвращает новую матрицу, транспонированную относительно исходной.
     *
     * @return новый экземпляр <code>Matrix</code>, являющийся транспонированной
     * относительно исходной
     */
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
