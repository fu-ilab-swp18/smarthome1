import { Sensor } from './sensor';

export interface SensorEvent {
    id: number;
    sensor: Sensor;
    value: number;
    old: boolean;
    time:Date;
}