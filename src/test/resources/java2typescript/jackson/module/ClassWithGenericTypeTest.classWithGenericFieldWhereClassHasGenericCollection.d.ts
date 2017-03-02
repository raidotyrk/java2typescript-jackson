export interface ClassWithGenericFieldWhereClassHasGenericCollection {
    genericClassOfStrings: ClassHasGenericCollection<string>;
    genericClassOfNonPrimitiveGeneric: ClassHasGenericCollection<BooleanClass>;
}

export interface ClassHasGenericCollection<T> {
    genericList: T[];
    genericMap: { [key: string ]: T;};
}

export interface BooleanClass {
    someField: boolean;
    genericList: boolean[];
}

