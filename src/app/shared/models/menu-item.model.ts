export interface MenuItem {
    label: string;
    icon?: string;
    labelText?:string;
    route?: string;
    roles?: string[];
    active: boolean;
    children?: MenuItem[];
 }
  