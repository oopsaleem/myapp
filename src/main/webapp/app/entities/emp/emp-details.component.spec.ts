/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EmpDetails from './emp-details.vue';
import EmpService from './emp.service';
import AlertService from '@/shared/alert/alert.service';

type EmpDetailsComponentType = InstanceType<typeof EmpDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const empSample = { id: 'ABC' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Emp Management Detail Component', () => {
    let empServiceStub: SinonStubbedInstance<EmpService>;
    let mountOptions: MountingOptions<EmpDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      empServiceStub = sinon.createStubInstance<EmpService>(EmpService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          empService: () => empServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        empServiceStub.find.resolves(empSample);
        route = {
          params: {
            empId: '' + 'ABC',
          },
        };
        const wrapper = shallowMount(EmpDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.emp).toMatchObject(empSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        empServiceStub.find.resolves(empSample);
        const wrapper = shallowMount(EmpDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
