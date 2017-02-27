export module mod {

    export class TestClassStatic {
        static MY_CONSTANT_ENUM: ChangedEnumName = ChangedEnumName.VAL1;
        static MY_CONSTANT_STRING: string = 'Test';
        static MY_CONSTANT_ENUM_ARRAY: ChangedEnumName[] = [ ChangedEnumName.VAL1 ];
        static MY_CONSTANT_DOUBLE: number = 42.12;
        static MY_CONSTANT_ARRAY: string[] = [ 'Test' ];
        static MY_CONSTANT_INT_ARRAY: number[] = [ 10, 12 ];
        static MY_CONSTANT_DOUBLE_ARRAY: number[] = [ 42.12 ];
        static MY_CONSTANT_BOOLEAN_ARRAY: boolean[] = [ true, false, true ];
        static MY_CONSTANT_BOOLEAN: boolean = true;
        static MY_CONSTANT_INT: number = 10;
        static MY_CONSTANT_ENUM_ARRAY_2: ChangedEnumName[] = [ ChangedEnumName.VAL1, ChangedEnumName.VAL2 ];
    }

    export enum ChangedEnumName {
        VAL1,
        VAL2,
        VAL3,
    }

}

