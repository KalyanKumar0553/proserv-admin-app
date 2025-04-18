import { Roles } from "./roles.enum";

export interface JwtPayload {
    sub: string;
    roles: Roles[];
    exp: number;
  }
  