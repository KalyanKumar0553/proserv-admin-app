export interface MenuItem {
    label: string;
    icon?: string;
    route?: string;
    roles?: string[];
    active: boolean;
    children?: MenuItem[];
 }
  