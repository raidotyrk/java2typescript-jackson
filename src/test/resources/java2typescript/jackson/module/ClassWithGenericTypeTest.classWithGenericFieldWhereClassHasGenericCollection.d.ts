export interface ClassWithGenericFieldWhereClassHasGenericCollection {
    genericClassOfStrings: ClassHasGenericCollection<string>;
}

export interface ClassHasGenericCollection<T> {
    genericList: T[];
}

