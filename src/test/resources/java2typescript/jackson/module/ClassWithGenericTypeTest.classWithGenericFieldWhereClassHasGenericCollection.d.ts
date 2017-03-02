export interface ClassWithGenericFieldWhereClassHasGenericCollection {
    genericClassOfStrings: ClassHasGenericCollection<string>;
    genericClassOfNonPrimitiveGeneric: ClassHasGenericCollection<BooleanClass>;
}

export interface ClassHasGenericCollection<T> {
    genericList: T[];
    genericMap: { [key: string ]: T;};
    genericValueClassField: ValueClass<T>;
    stringValueClassField: ValueClass<string>;
    nonPrimitiveValueClassField: ValueClass<BooleanClass>;
}

export interface BooleanClass {
    someField: boolean;
    genericList: boolean[];
}

export interface ValueClass<T> {
    genericValue: T;
}
