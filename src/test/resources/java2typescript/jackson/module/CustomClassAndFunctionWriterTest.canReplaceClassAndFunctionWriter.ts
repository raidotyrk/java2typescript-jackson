export interface RequestClass<T> {
    genericList: T[];
}

export interface ResponseDto {
    stringField: string;
}

export interface RegularClass {
    find(param0: RequestClass<string>): Page<ResponseDto>;
}

export interface Page<T> {
    genericList: T[];
}

export class MyController {
    find(param0: RequestClass<string>): Page<ResponseDto> {
        // could invoke api endpoint
        return null;
    };
}

