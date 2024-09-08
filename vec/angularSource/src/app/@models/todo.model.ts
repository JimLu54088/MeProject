export interface Todo {
    Status: boolean;
    Thing: string;
    Editing: boolean;
    TodoId: string;
    CanEdit: boolean;
    Seqno: number;
    CreateTime: Date;
    AccountId?: string;
    GroupId?: string;
}

export class TodoClass implements Todo {
    Status: boolean;
    Thing: string;
    Editing: boolean;
    TodoId: string;
    CanEdit: boolean;
    Seqno: number;
    CreateTime: Date;

    constructor(_thing: string, _status: boolean = false, _seqno: number) {
        this.Thing = _thing;
        this.Status = _status;
        this.Editing = false;
        this.TodoId = '';
        this.CanEdit = false;
        this.Seqno = _seqno;
        this.CreateTime = new Date();
    }

    toggle() {
        this.Status = !this.Status;
    }
}


export enum TodoStatusType {
    All,
    Active,
    Completed
}