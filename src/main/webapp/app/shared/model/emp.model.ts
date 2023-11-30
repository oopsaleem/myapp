export interface IEmp {
  id?: string;
  firstName?: string | null;
  lastName?: string | null;
}

export class Emp implements IEmp {
  constructor(
    public id?: string,
    public firstName?: string | null,
    public lastName?: string | null,
  ) {}
}
