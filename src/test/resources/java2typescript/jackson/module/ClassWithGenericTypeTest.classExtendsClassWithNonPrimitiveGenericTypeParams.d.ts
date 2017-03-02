export interface ClassWithNonPrimitiveGeneric {
    genericValue: BooleanClass;
}

export interface BooleanClass {
    someField: boolean;
    genericList: boolean[];
}

