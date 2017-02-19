export interface Response {
    stringValueClassField: ValueClass<string>;
}

export interface ValueClass<T> {
    genericValue: T;
}
