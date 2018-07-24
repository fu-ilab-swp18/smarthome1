import { AlcSensor } from './alcSensor';

export interface AlcSensorEvent {
    id: number;
    alcSensor: AlcSensor;
    value: number;
}
