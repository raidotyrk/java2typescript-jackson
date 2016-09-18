/**
 * At the moment superclasses of Java class are not reflected by TS interface,
 * fields from superclass GenericClass<String> are inlined
 */
export interface StringClass {
    someField: string;
    genericList: string[];
}

export interface BooleanClass {
    someField: boolean;
    genericList: boolean[];
}
