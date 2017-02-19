export interface ClassWithCollections {
    // collections (List<T> and Collection<T>) are converted into arrays (T[])
    stringList: string[];
    booleanCollection: boolean[];
    booleanWrapperArray: boolean[];
    booleanPrimitiveArray: boolean[];
    longPrimitiveArray: number[];
    longWrapperArray: number[];
}
