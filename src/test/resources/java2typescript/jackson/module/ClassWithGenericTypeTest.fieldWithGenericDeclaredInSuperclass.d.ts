export interface Page<T> {
    // FIXME type is incorrectly `any[]`
    listFieldInSliceClass: T[];
    // OK
    listFieldInPageClass: T[];
}

