export interface IDept {
  id?: string;
  deptName?: string | null;
  deptAddress?: string | null;
}

export class Dept implements IDept {
  constructor(
    public id?: string,
    public deptName?: string | null,
    public deptAddress?: string | null,
  ) {}
}
