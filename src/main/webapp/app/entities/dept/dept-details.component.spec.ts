/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DeptDetails from './dept-details.vue';
import DeptService from './dept.service';
import AlertService from '@/shared/alert/alert.service';

type DeptDetailsComponentType = InstanceType<typeof DeptDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const deptSample = { id: 'ABC' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Dept Management Detail Component', () => {
    let deptServiceStub: SinonStubbedInstance<DeptService>;
    let mountOptions: MountingOptions<DeptDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      deptServiceStub = sinon.createStubInstance<DeptService>(DeptService);

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
          deptService: () => deptServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        deptServiceStub.find.resolves(deptSample);
        route = {
          params: {
            deptId: '' + 'ABC',
          },
        };
        const wrapper = shallowMount(DeptDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dept).toMatchObject(deptSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        deptServiceStub.find.resolves(deptSample);
        const wrapper = shallowMount(DeptDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
