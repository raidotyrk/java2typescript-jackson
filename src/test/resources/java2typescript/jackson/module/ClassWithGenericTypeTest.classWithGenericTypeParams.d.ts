export interface ClassWithGenericTypeParams<K, V> {
    stringField: string;
    genericFieldK: K;
    genericFieldV: V;
    booleansByStrings: { [key: string ]: boolean;};
}

