export interface ClassWithMaps {
    mapStringToBoolean: { [key: string ]: boolean;};
    mapIntegerToBoolean: { [key: number ]: boolean;};
    mapAtomicIntegerToBoolean: { [key: number ]: boolean;};
    mapLongToBoolean: { [key: number ]: boolean;};
    mapDoubleToString: { [key: number ]: string;};
    mapIntegerToCustomType: { [key: number ]: ClassWithMaps;};
}

