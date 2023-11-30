/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DeptUpdate from './dept-update.vue';
import DeptService from './dept.service';
import AlertService from '@/shared/alert/alert.service';

type DeptUpdateComponentType = InstanceType<typeof DeptUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const deptSample = { id: 'ABC' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DeptUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Dept Management Update Component', () => {
    let comp: DeptUpdateComponentType;
    let deptServiceStub: SinonStubbedInstance<DeptService>;

    beforeEach(() => {
      route = {};
      deptServiceStub = sinon.createStubInstance<DeptService>(DeptService);
      deptServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          deptService: () => deptServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(DeptUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dept = deptSample;
        deptServiceStub.update.resolves(deptSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(deptServiceStub.update.calledWith(deptSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        deptServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DeptUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dept = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(deptServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        deptServiceStub.find.resolves(deptSample);
        deptServiceStub.retrieve.resolves([deptSample]);

        // WHEN
        route = {
          params: {
            deptId: '' + deptSample.id,
          },
        };
        const wrapper = shallowMount(DeptUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dept).toMatchObject(deptSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        deptServiceStub.find.resolves(deptSample);
        const wrapper = shallowMount(DeptUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
